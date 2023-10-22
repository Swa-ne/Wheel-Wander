package me.tepen.wheelwander.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
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

class HomeFragment : Fragment() {

    //  TODO: CHANGE URL EVERYTIME
    private val BASE_URL : String = "http://192.168.1.120:3000/"

    private lateinit var retrofit: Retrofit
    private lateinit var marketInterface : MarketInterface
    private lateinit var rentedVehicleRecyclerView : RecyclerView
    private lateinit var vehicleList : ArrayList<Vehicles>
    private lateinit var adapter: TopVehicleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        view.findViewById<TextView>(R.id.seeAllButton).setOnClickListener{
            openMarketFragment("")
        }
        view.findViewById<ConstraintLayout>(R.id.motorcycle).setOnClickListener{
            openMarketFragment("Motorcycle")
        }
        view.findViewById<ConstraintLayout>(R.id.car).setOnClickListener{
            openMarketFragment("Car")
        }
        view.findViewById<ConstraintLayout>(R.id.suv).setOnClickListener{
            openMarketFragment("SUV")
        }
        view.findViewById<ConstraintLayout>(R.id.van).setOnClickListener{
            openMarketFragment("Van")
        }
        view.findViewById<ConstraintLayout>(R.id.truck).setOnClickListener{
            openMarketFragment("Pickup Truck")
        }

        retrofit = APIClient().getClient()!!

        marketInterface = retrofit.create(MarketInterface::class.java)


        vehicleList = ArrayList()

        var call : Call<GetVehicles> = marketInterface.getBestVehicles()
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
                                vehicleList.add(
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
                        if(vehicleList.size == 0){
                            view.findViewById<TextView>(R.id.empty).visibility = VISIBLE
                        }else{
                            view.findViewById<TextView>(R.id.empty).visibility = INVISIBLE
                        }

                        adapter = TopVehicleAdapter(vehicleList)

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private suspend fun getBitmap(url : String) : Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }


    private fun openMarketFragment(filter : String) {
        val marketFragment = MarketFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        if (!filter.isNullOrBlank()){
            val args = Bundle()
            args.putString("filter", filter)
            marketFragment.arguments = args
        }
        transaction.replace(R.id.fragmentContainerView, marketFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}