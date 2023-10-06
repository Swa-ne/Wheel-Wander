package me.tepen.wheelwander

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import me.tepen.wheelwander.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var retrofit: Retrofit
    private lateinit var entryInterface : EntryInterface
    private lateinit var intent : Intent

    private val BASE_URL : String = "http://192.168.1.90:3000"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        entryInterface = retrofit.create(EntryInterface::class.java)

        binding.signUpButton.setOnClickListener{
            handleSignup()
        }
        binding.loginButton.setOnClickListener{
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun handleSignup() {

        var map : HashMap<String, String> = HashMap()
        map["username"] = binding.usernameEditText.text.toString()
        map["emailAddress"] = binding.emailEditText.text.toString()
        map["confirmationEmailAddress"] = binding.confirmEmailEditText.text.toString()
        map["password"] = binding.passwordEditText.text.toString()
        map["confirmationPassword"] = binding.confirmPasswordEditText.text.toString()

        var call : Call<LoginResult> = entryInterface.executeSignup(map)
        call.enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                if(response.isSuccessful) {
                    var responseBodyMessage : String? = response.body()?.message

                    if(responseBodyMessage == "success") {
                        intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.errorText.text = responseBodyMessage
                    }
                } else {
                    Toast.makeText(applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                println("SwaneErr: " + t + call)
            }
        })
    }
}