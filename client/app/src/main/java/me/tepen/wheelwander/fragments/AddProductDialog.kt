package me.tepen.wheelwander.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import me.tepen.wheelwander.R
import me.tepen.wheelwander.adapters.AddProductDialogAdapter
import me.tepen.wheelwander.databinding.AddProductDialogBinding
import me.tepen.wheelwander.interfaces.UploadCarInterface
import me.tepen.wheelwander.models.UploadResult
import me.tepen.wheelwander.network.APIClient
import me.tepen.wheelwander.utils.EncryptSharedPreference
import me.tepen.wheelwander.utils.RealPathUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.util.Locale
import kotlin.properties.Delegates


class AddProductDialog : DialogFragment() {
    val TAG = "add_product"

    private var FINE_PERMISSION_CODE = 1;
    private lateinit var toolbar : Toolbar
    private lateinit var intent : Intent
    private lateinit var uploadCarInterface: UploadCarInterface
    private lateinit var retrofit: Retrofit
    private lateinit var ImageUri : Uri
    private lateinit var chosenImageList : ArrayList<Uri>
    private var long : Double = 0.0
    private var lat : Double = 0.0
    private lateinit var currentLocation : Location
    private lateinit var binding : AddProductDialogBinding
    private lateinit var fusedLocationProvidedClient : FusedLocationProviderClient

    fun display(fragmentManager: FragmentManager?): AddProductDialog {
        val addProductDialog = AddProductDialog()
        if (fragmentManager != null) {
            addProductDialog.show(fragmentManager, TAG)
        }
        return addProductDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        retrofit = APIClient().getClient()!!
        uploadCarInterface = retrofit.create(UploadCarInterface::class.java)

        chosenImageList = ArrayList()
        binding = AddProductDialogBinding.inflate(inflater, container, false)
        toolbar = binding.toolbar
        fusedLocationProvidedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.getCurrentLocation.setOnClickListener{
            getLastLocation()
        }
        binding.chooseImage.setOnClickListener {
            pickImageFromGallery()
        }
        binding.uploadButton.setOnClickListener {
            uploadVehicle()
        }
        return binding.root
    }

    private fun uploadVehicle() {


        var map : HashMap<String, String> = HashMap()
        map["userID"] = activity?.let { EncryptSharedPreference(it.applicationContext).getEncryptedSharedPreference()?.getString("userID", "") }.toString()
        map["type"] = binding.typeVehicleEt.text.toString()
        map["brand"] = binding.brandVehicleEt.text.toString()
        map["model"] = binding.modelVehicleEt.text.toString()
        map["plateNumber"] = binding.plateNumberEt.text.toString()
        map["fuel"] = binding.fuelVehicleEt.text.toString()
        map["description"] = binding.descriptionEt.text.toString()
        map["location"] = binding.locationEt.text.toString()
        map["longitude"] = long.toString()
        map["latitude"] = lat.toString()
        map["price"] = binding.price.text.toString()
        map["timeFrame"] = binding.timeFrame.text.toString()

        var token : String? = EncryptSharedPreference(requireActivity()).getEncryptedSharedPreference()?.getString("token", null)
        val list : ArrayList<MultipartBody.Part> = ArrayList()
        for (uri in chosenImageList){
            list.add(prepareImagePart(uri, "image"))
        }

        var call2 : Call<UploadResult> = uploadCarInterface.uploadVehicleInformation(token, map)
        call2.enqueue(object : Callback<UploadResult> {
            override fun onResponse(call: Call<UploadResult>, response: Response<UploadResult>) {
                if(response.isSuccessful) {
                    var responseBodyMessage : String? = response.body()?.message

                    if(responseBodyMessage == "success") {
                    } else {
                        Toast.makeText(requireActivity(), "2Server Error!200", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireActivity(), "2Server Error!500", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UploadResult>, t: Throwable) {
                Log.d("AMAZING", "NOT WORKING")
            }
        })
        var call1 : Call<UploadResult> = uploadCarInterface.uploadVehicleImage(list, map["plateNumber"], map["type"])
        call1.enqueue(object : Callback<UploadResult> {
            override fun onResponse(call: Call<UploadResult>, response: Response<UploadResult>) {
                if(response.isSuccessful) {
                    var responseBodyMessage : String? = response.body()?.message

                    if(responseBodyMessage == "success") {
                    } else {
                        Toast.makeText(requireActivity(), "1Server Error200!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireActivity(), "1Server Error!500", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UploadResult>, t: Throwable) {
                Log.d("AMAZING", "NOT WORKING")
            }
        })
        this.dismiss()
    }

    private fun pickImageFromGallery() {
        val intent : Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null && data.clipData != null){
            val count : Int = data.clipData!!.itemCount
            for (i in 0 until count){
                ImageUri = data.clipData!!.getItemAt(i).uri
                chosenImageList.add(ImageUri)
                setAdapter()
            }
        }
        if (data != null && data.data != null) {
            ImageUri = data.data!!
            chosenImageList.add(ImageUri)
            setAdapter()
        }
    }

    private fun setAdapter() {

        val adapter : AddProductDialogAdapter? =
            context?.let { AddProductDialogAdapter(it, chosenImageList) }
        binding.chosenImage.adapter = adapter
    }

    private fun getLastLocation() {

        if(this.context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvidedClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {

                    val geocoder : Geocoder? = context?.let { Geocoder(it, Locale.getDefault()) }
                    val address : MutableList<Address>? =
                        geocoder?.getFromLocation(location.latitude, location.longitude, 1)
                    if (address != null) {
                        binding.locationEt.setText(address[0].getAddressLine(0))
                        long = address[0].longitude
                        lat = address[0].latitude
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Upload Vehicle"
        toolbar.inflateMenu(R.menu.add_product_dialog)
        toolbar.setOnMenuItemClickListener { item ->
            dismiss()
            true
        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private fun prepareImagePart (path : Uri, partName : String) : MultipartBody.Part {
        val realPath = RealPathUtil.getRealPath(activity?.applicationContext, path)
        val file: File = File(realPath)
        val requestBody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}