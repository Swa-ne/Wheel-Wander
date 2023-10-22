package me.tepen.wheelwander.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.tepen.wheelwander.R
import me.tepen.wheelwander.activities.MainChatActivity
import me.tepen.wheelwander.activities.VehicleRentingLayout
import me.tepen.wheelwander.models.Vehicles

class TopVehicleAdapter (val vehicleList : ArrayList<Vehicles>) :
    RecyclerView.Adapter<TopVehicleAdapter.UserViewHolder>() {

    private lateinit var contextParent : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        contextParent = parent
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_layout, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = vehicleList[position]
        holder.name.text = currentUser.name
        holder.type.text = currentUser.type
        holder.rate.text = currentUser.rate
        holder.mainImage.setImageBitmap(currentUser.mainImage)
        holder.itemView.setOnClickListener{
//        TODO: CHANGE INTENT
            val intent = Intent(contextParent.context, VehicleRentingLayout::class.java)
            intent.putExtra("vehicleID", currentUser.ID)
            contextParent.context.startActivity(intent)
        }
    }
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)
        val type = itemView.findViewById<TextView>(R.id.type)
        val rate = itemView.findViewById<TextView>(R.id.rate)
        val mainImage = itemView.findViewById<ImageView>(R.id.mainImage)
    }
}