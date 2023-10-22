package me.tepen.wheelwander.network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import me.tepen.wheelwander.models.ChatResult
import java.net.URISyntaxException

class SocketHandler {

    private var socket: Socket? = null

    private val _onNewChat = MutableLiveData<ChatResult>()
    val onNewChat: LiveData<ChatResult> get() = _onNewChat

    init {
        try {
            socket = IO.socket(SOCKET_URL)
            socket?.connect()

            registerOnNewChat()

        }catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private fun registerOnNewChat() {
        socket?.on(CHAT_KEYS.BROADCAST) { args->
            args?.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    if (data.toString().isNotEmpty()) {
                        val chat = Gson().fromJson(data.toString(), ChatResult::class.java)
                        _onNewChat.postValue(chat)
                    }
                }

            }
        }
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
    }


    fun emitChat(chat: ChatResult) {
        val jsonStr = Gson().toJson(chat, ChatResult::class.java)
        socket?.emit(CHAT_KEYS.NEW_MESSAGE, jsonStr)
    }

    private object CHAT_KEYS {
        const val NEW_MESSAGE = "new_message"
        const val BROADCAST = "broadcast"
    }

    companion object{
        private const val SOCKET_URL = "http://10.0.2.2:3000/"
    }

}