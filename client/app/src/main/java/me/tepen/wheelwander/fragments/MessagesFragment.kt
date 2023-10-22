package me.tepen.wheelwander.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import io.socket.engineio.client.Socket
import kotlinx.coroutines.launch
import me.tepen.wheelwander.R
import me.tepen.wheelwander.adapters.UserMessageAdapter
import me.tepen.wheelwander.models.MessageUserInbox

class MessagesFragment : Fragment() {

    private lateinit var chatRecycleView : RecyclerView
    private lateinit var userList : ArrayList<MessageUserInbox>
    private lateinit var adapter: UserMessageAdapter
    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            userList = ArrayList()

           userList.add(MessageUserInbox("2","Phew", "Hello!", "11:11AM", getBitmap("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=1024x1024&w=is&k=20&c=-mUWsTSENkugJ3qs5covpaj-bhYpxXY-v9RDpzsw504=")))
            if(userList.size > 0){
                view.findViewById<TextView>(R.id.messageHistory).text = ""
            } else {
                view.findViewById<TextView>(R.id.messageHistory).text = "No Chat History"
            }
            adapter = UserMessageAdapter(userList)

            chatRecycleView = view.findViewById(R.id.messageRecyclerView)
            chatRecycleView.layoutManager = LinearLayoutManager(context)

            chatRecycleView.adapter = adapter
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