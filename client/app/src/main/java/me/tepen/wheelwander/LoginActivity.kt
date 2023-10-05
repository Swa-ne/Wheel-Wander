package me.tepen.wheelwander

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import me.tepen.wheelwander.databinding.LoginActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var retrofit: Retrofit
    private lateinit var entryInterface : EntryInterface
    private lateinit var intent : Intent

    private val BASE_URL : String = "http://192.168.1.92:3000"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        entryInterface = retrofit.create(EntryInterface::class.java)

        binding.loginButton.setOnClickListener{
            handleLogin()
        }
        binding.signUpButton.setOnClickListener{
            intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {

        var map : HashMap<String, String> = HashMap()
        map["userIdentifier"] = binding.emailEditText.text.toString()
        map["password"] = binding.passwordEditText.text.toString()

        var call : Call<LoginResult> = entryInterface.executeLogin(map)
        call.enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                if(response.isSuccessful) {
                    var responseBodyMessage : String? = response.body()?.message
                    var responseBodyToken : String? = response.body()?.accessToken
                    
                    if(responseBodyMessage == "success") {
                        getEncryptedSharedPreference()?.edit()?.putString("token", responseBodyToken)
                        intent = Intent(applicationContext, MainActivity::class.java)
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

    private fun getEncryptedSharedPreference() : SharedPreferences? {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "Asgjshffeakjdslffjlaskdjflasdl;kjflasdf",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}