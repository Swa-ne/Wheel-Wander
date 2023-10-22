package me.tepen.wheelwander.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.tepen.wheelwander.R
import me.tepen.wheelwander.adapters.TopVehicleAdapter
import me.tepen.wheelwander.interfaces.MarketInterface
import me.tepen.wheelwander.models.GetVehicles
import me.tepen.wheelwander.models.Vehicles
import me.tepen.wheelwander.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MarketFragment : Fragment() {

//  TODO: CHANGE URL EVERYTIME
    private val BASE_URL : String = "http://192.168.1.120:3000/"

    private lateinit var retrofit: Retrofit
    private lateinit var marketInterface : MarketInterface
    private lateinit var rentedVehicleRecyclerView : RecyclerView
    private lateinit var vehicleDetailsList : ArrayList<Vehicles>
    private lateinit var adapter: TopVehicleAdapter
    private lateinit var view: View
    private lateinit var contextForBitmap: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_market, container, false)
        retrofit = APIClient().getClient()!!

        marketInterface = retrofit.create(MarketInterface::class.java)
        updatePage()
        view.findViewById<FloatingActionButton>(R.id.floating_action_button).setOnClickListener {
            AddProductDialog().display(fragmentManager)
        }
        view.findViewById<AutoCompleteTextView>(R.id.filterVehicles).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val filter = s.toString()
                Log.d("AMAZINGsss", filter)

                vehicleDetailsList = ArrayList()
                vehicleDetailsList.clear()
                var call1 : Call<GetVehicles> = when(filter){
                    "Motorcycle" -> marketInterface.getVehiclesMotorcycle()
                    "Car" -> marketInterface.getVehiclesCar()
                    "SUV" -> marketInterface.getVehiclesSUV()
                    "Van" -> marketInterface.getVehiclesVan()
                    "Pickup Truck" -> marketInterface.getVehiclesTruck()
                    else -> marketInterface.getVehicles()
                }

                call1.enqueue(object : Callback<GetVehicles> {
                    override fun onResponse(call: Call<GetVehicles>, response: Response<GetVehicles>) {
                        if(response.isSuccessful) {
                            var vehicleDetails = response.body()?.vehicleDetails
                            var images = response.body()?.mainImage
                            lifecycleScope.launch {
                                var i = 0
                                if (vehicleDetails != null) {
                                    for (vehicleDetail in vehicleDetails){
                                        vehicleDetailsList.add(Vehicles(vehicleDetail.VehicleID.toString(), vehicleDetail.VehicleType, "${vehicleDetail.VehicleBrand} ${vehicleDetail.VehicleModel}", getBitmap("${BASE_URL}${images?.get(i)?.ImagePath}"), "₱${vehicleDetail.Price} ${vehicleDetail.TimeFrame}"))
                                        i++
                                    }

                                }
                                if(vehicleDetailsList.size == 0){
                                    view.findViewById<TextView>(R.id.availableTv).text = "No Available Vehicle"
                                }else{
                                    view.findViewById<TextView>(R.id.availableTv).text = "Available Vehicle"
                                }

                                adapter = TopVehicleAdapter(vehicleDetailsList)

                                rentedVehicleRecyclerView = view.findViewById(R.id.rentedVehicleRecyclerView)
                                rentedVehicleRecyclerView.layoutManager = GridLayoutManager(context, 2)

                                rentedVehicleRecyclerView.adapter = adapter
                            }
                        } else {
                            Toast.makeText(activity?.applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GetVehicles>, t: Throwable) {
                        Toast.makeText(activity?.applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextForBitmap = context
    }
    private fun updatePage() {
        vehicleDetailsList = ArrayList()
        vehicleDetailsList.clear()
        var call : Call<GetVehicles> = marketInterface.getVehicles()
        call.enqueue(object : Callback<GetVehicles> {
            override fun onResponse(call: Call<GetVehicles>, response: Response<GetVehicles>) {
                if(response.isSuccessful) {
                    var vehicleDetails = response.body()?.vehicleDetails
                    var images = response.body()?.mainImage
                    lifecycleScope.launch {
                        var i = 0
                        if (vehicleDetails != null) {
                            for (vehicleDetail in vehicleDetails){
                                val bitmapURL = "${BASE_URL}${images?.get(i)?.ImagePath}"
                                vehicleDetailsList.add(
                                    Vehicles(
                                        vehicleDetail.VehicleID.toString(),
                                        vehicleDetail.VehicleType,
                                        "${vehicleDetail.VehicleBrand} ${vehicleDetail.VehicleModel}",
                                        getBitmap(bitmapURL),
                                        "₱${vehicleDetail.Price} ${vehicleDetail.TimeFrame}"
                                    )
                                )
                                i++
                            }
                        }
                        if(vehicleDetailsList.size == 0){
                            view.findViewById<TextView>(R.id.availableTv).text = "No Available Vehicle"
                        }else{
                            view.findViewById<TextView>(R.id.availableTv).text = "Available Vehicle"
                        }

                        adapter = TopVehicleAdapter(vehicleDetailsList)

                        rentedVehicleRecyclerView = view.findViewById(R.id.rentedVehicleRecyclerView)
                        rentedVehicleRecyclerView.layoutManager = GridLayoutManager(context, 2)

                        rentedVehicleRecyclerView.adapter = adapter
                    }
                } else {
                    Toast.makeText(activity?.applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetVehicles>, t: Throwable) {
                Toast.makeText(activity?.applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private suspend fun getBitmap(url : String) : Bitmap {
        Log.d("AMAZINGzs", "Runsss")
        val loading = ImageLoader(contextForBitmap)
        val request = ImageRequest.Builder(contextForBitmap)
            .data(url)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}