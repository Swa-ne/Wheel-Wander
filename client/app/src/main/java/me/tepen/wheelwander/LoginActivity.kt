package me.tepen.wheelwander

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import me.tepen.wheelwander.databinding.LoginActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var retrofit: Retrofit
    private lateinit var EntryInterface: EntryInterface

    private val BASE_URL : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        EntryInterface = retrofit.create(EntryInterface.javaClass)

        binding.loginButton.setOnClickListener{
            handleLoginDialog()
        }
    }

    private fun handleLoginDialog() {
        var view : View = layoutInflater.inflate(R.layout.login_activity, null)
        var builder : AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setView(view).show()

        binding.loginButton.setOnClickListener {
            var map : HashMap<String, String> = HashMap<String, String>()
            map["email"] = binding.emailEditText.text.toString()
            map["password"] = binding.passwordEditText.text.toString()

            var call : Call<LoginResult> = EntryInterface.executeLogin(map)
            call.enqueue(object : Callback<LoginResult> {
                override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {

                }

                override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}