package me.tepen.wheelwander.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import me.tepen.wheelwander.R
import me.tepen.wheelwander.adapters.ImageCarouselAdapter
import me.tepen.wheelwander.adapters.TopVehicleAdapter
import me.tepen.wheelwander.databinding.ActivityVehicleRentingLayoutBinding
import me.tepen.wheelwander.databinding.LoginActivityBinding
import me.tepen.wheelwander.databinding.VehicleLayoutBinding
import me.tepen.wheelwander.interfaces.MarketInterface
import me.tepen.wheelwander.models.GetVehicles
import me.tepen.wheelwander.models.Vehicles
import me.tepen.wheelwander.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class VehicleRentingLayout : AppCompatActivity() {

    //  TODO: CHANGE URL EVERYTIME
    private val BASE_URL : String = "http://192.168.1.120:3000/"

    private lateinit var binding: ActivityVehicleRentingLayoutBinding
    private lateinit var retrofit: Retrofit
    private lateinit var marketInterface : MarketInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleRentingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = APIClient().getClient()!!

        marketInterface = retrofit.create(MarketInterface::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val arrayList = ArrayList<String>()
        val vehicleID = intent.getStringExtra("vehicleID")

        var call1 : Call<GetVehicles> = marketInterface.getVehiclesID(vehicleID)
        call1.enqueue(object : Callback<GetVehicles> {
            override fun onResponse(call: Call<GetVehicles>, response: Response<GetVehicles>) {
                if(response.isSuccessful) {
                    var vehicleDetails = response.body()?.vehicleDetails
                    Log.d("AMAZING1s", vehicleDetails.toString())



                    binding.vehicleName.text = vehicleDetails?.get(0)?.VehicleModel
                    binding.vehicleRented.text = "${vehicleDetails?.get(0)?.TimesRented}x Rented"
                    binding.vehiclePrice.text = "${vehicleDetails?.get(0)?.Price} ${vehicleDetails?.get(0)?.TimeFrame}"
                    binding.ownerName.text = vehicleDetails?.get(0)?.UserName
                    binding.description.text = vehicleDetails?.get(0)?.Description
                    binding.vehicleType.text = vehicleDetails?.get(0)?.VehicleType
                    binding.vehicleBrand.text = vehicleDetails?.get(0)?.VehicleBrand
                    binding.plateNumber.text = vehicleDetails?.get(0)?.PlateNumber
                    binding.fuel.text = vehicleDetails?.get(0)?.Fuel
                    binding.location.text = vehicleDetails?.get(0)?.Location



                    var images = response.body()?.mainImage
                    if (images != null) {
                        for (image in images){
                            val imagePath = replaceCharAtIndex(image.ImagePath, 6, '/')
                            var IMG_URL = "${BASE_URL}${imagePath}"
                            arrayList.add(IMG_URL);
                        }
                    }
                    val adapter = ImageCarouselAdapter(this@VehicleRentingLayout, arrayList)
                    recyclerView.adapter = adapter

                    adapter.setOnItemClickListener(object : ImageCarouselAdapter.OnItemClickListener {
                        override fun onClick(imageView: ImageView?, path: String?) {
                            startActivity(
                                Intent(
                                    this@VehicleRentingLayout,
                                    ImageViewActivity::class.java
                                ).putExtra("image", path),
                                ActivityOptions.makeSceneTransitionAnimation(
                                    this@VehicleRentingLayout,
                                    imageView,
                                    "image"
                                ).toBundle()
                            )
                        }
                    })
                } else {
                    Toast.makeText(applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetVehicles>, t: Throwable) {
                Toast.makeText(applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun replaceCharAtIndex(input: String, index: Int, newChar: Char): String {
        if (index < 0 || index >= input.length) {
            return input
        }

        val charArray = input.toCharArray()

        charArray[index] = newChar

        return String(charArray)
    }
}