package me.tepen.wheelwander.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.tepen.wheelwander.R
import me.tepen.wheelwander.models.Message
import me.tepen.wheelwander.utils.EncryptSharedPreference

class MessageChatAdapter(val messageList : ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    private lateinit var parentContext : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        parentContext = parent
        if (viewType == 1) {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.recieve, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage : Message = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        var userID : String? = EncryptSharedPreference(parentContext.context).getEncryptedSharedPreference()?.getString("userID", null)

        return if(currentMessage.senderID == userID){
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sent_message)
    }
    class ReceiveViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val receiveMessage = itemView.findViewById<TextView>(R.id.receive_message)
    }
}