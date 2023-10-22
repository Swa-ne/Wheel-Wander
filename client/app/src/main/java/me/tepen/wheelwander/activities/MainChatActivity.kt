package me.tepen.wheelwander.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import me.tepen.wheelwander.R
import me.tepen.wheelwander.adapters.MessageChatAdapter
import me.tepen.wheelwander.databinding.ActivityMainChatBinding
import me.tepen.wheelwander.interfaces.EntryInterface
import me.tepen.wheelwander.interfaces.MainChatInterface
import me.tepen.wheelwander.models.ChatResult
import me.tepen.wheelwander.models.LoginResult
import me.tepen.wheelwander.models.Message
import me.tepen.wheelwander.network.APIClient
import me.tepen.wheelwander.utils.EncryptSharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URISyntaxException

class MainChatActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMainChatBinding
    private lateinit var messageChatAdapter : MessageChatAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var socket : Socket

    private lateinit var retrofit: Retrofit
    private lateinit var mainChatInterface : MainChatInterface

    val receiverRoom: String? = null
    val senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        val userIDReceiver = intent.getStringExtra("userIDReceiver")

        val userIDSender = EncryptSharedPreference(this@MainChatActivity).getEncryptedSharedPreference()?.getString("userID", null)


        retrofit = APIClient().getClient()!!

        mainChatInterface = retrofit.create(MainChatInterface::class.java)

        messageList = ArrayList()
        messageChatAdapter = MessageChatAdapter(messageList)

        binding.toolbar.title = username
        binding.messageRecyclerView
        binding.sendButton.setOnClickListener{
            try {
                socket = IO.socket(SOCKET_URL)
                socket.connect()
                val msg = binding.messageEditText.text.toString()
                val data = Gson().toJson(Message(msg, userIDSender))
                socket.emit("send-msg", data)

//                TODO: Put message to Client screen
//                addMessagetoClientScreen(msg)
//                TODO: Put message to database
                addMessagetoDatabase(userIDSender, userIDReceiver, msg)

                binding.messageEditText.setText("")
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }


    }

    private fun addMessagetoDatabase(userIDSender : String?, userIDReceiver : String?, msg: String) {
        var map : HashMap<String, String?> = HashMap()
        map["senderID"] = userIDSender
        map["receiverID"] = userIDReceiver
        map["message"] = msg
        var token : String? = EncryptSharedPreference(this@MainChatActivity).getEncryptedSharedPreference()?.getString("token", null)
        if (token == null) goToLogin()

        var call : Call<ChatResult> = mainChatInterface.sendMessage(token, map)
        call.enqueue(object : Callback<ChatResult> {
            override fun onResponse(call: Call<ChatResult>, response: Response<ChatResult>) {
                println("Swane: $response")
                if(response.isSuccessful) {
                    var responseBodyMessage : String? = response.body()?.message

                    if(responseBodyMessage == "success") {
                        messageList.add(Message(msg, userIDSender))
                    } else {
//                        TODO: Create error msg not sent
                    }
                } else {
                    Toast.makeText(applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChatResult>, t: Throwable) {
                println("SwaneErrs: " + t + call)
            }
        })
    }

    private fun addMessagetoClientScreen(msg : String) {

    }

    companion object{
        private const val SOCKET_URL = "http://192.168.1.97:3000"
    }
    private fun goToLogin(){
        intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}