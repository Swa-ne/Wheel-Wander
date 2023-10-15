package me.tepen.wheelwander.adapters

import android.content.Context
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
import me.tepen.wheelwander.models.MessageUserInbox

class UserMessageAdapter(val userList : ArrayList<MessageUserInbox>) :
    RecyclerView.Adapter<UserMessageAdapter.UserViewHolder>() {

    private lateinit var contextParent : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        contextParent = parent
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.username.text = currentUser.username
        holder.message.text = currentUser.message
        holder.time.text = currentUser.time
        holder.avatar.setImageBitmap(currentUser.avatar)

        holder.itemView.setOnClickListener{
            val intent = Intent(contextParent.context, MainChatActivity::class.java)
            intent.putExtra("username", currentUser.username)
            intent.putExtra("userIDReceiver", currentUser.userID)
            contextParent.context.startActivity(intent)
        }
    }
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.username)
        val message = itemView.findViewById<TextView>(R.id.message)
        val time = itemView.findViewById<TextView>(R.id.time)
        val avatar = itemView.findViewById<ImageView>(R.id.avatar)
    }

}