package me.tepen.wheelwander.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import me.tepen.wheelwander.models.Vehicles

class HomeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            vehicleList = ArrayList()
//            TODO: fetch top vehicles
            vehicleList.add(Vehicles("1", "SUV", "Basta Kotse", getBitmap("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=-mUWsTSENkugJ3qs5covpaj-bhYpxXY-v9RDpzsw504="), "$200/hour"))
            vehicleList.add(Vehicles("2", "Car", "Basta Kotse", getBitmap("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=-mUWsTSENkugJ3qs5covpaj-bhYpxXY-v9RDpzsw504="), "$200/hour"))
            vehicleList.add(Vehicles("3", "Truck", "Basta Kotse", getBitmap("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=-mUWsTSENkugJ3qs5covpaj-bhYpxXY-v9RDpzsw504="), "$200/hour"))
            vehicleList.add(Vehicles("4", "Van", "Basta Kotse", getBitmap("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=-mUWsTSENkugJ3qs5covpaj-bhYpxXY-v9RDpzsw504="), "$200/hour"))
            vehicleList.add(Vehicles("5", "Motorcycle", "Basta Kotse", getBitmap("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=-mUWsTSENkugJ3qs5covpaj-bhYpxXY-v9RDpzsw504="), "$200/hour"))
            adapter = TopVehicleAdapter(vehicleList)

            rentedVehicleRecyclerView = view.findViewById(R.id.rentedVehicleRecyclerView)
            rentedVehicleRecyclerView.layoutManager = GridLayoutManager(context, 2)

            rentedVehicleRecyclerView.adapter = adapter
        }
    }
    private suspend fun getBitmap(url : String) : Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}