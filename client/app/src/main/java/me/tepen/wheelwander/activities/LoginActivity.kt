package me.tepen.wheelwander.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.tepen.wheelwander.databinding.LoginActivityBinding
import me.tepen.wheelwander.interfaces.EntryInterface
import me.tepen.wheelwander.models.LoginResult
import me.tepen.wheelwander.network.APIClient
import me.tepen.wheelwander.utils.EncryptSharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var retrofit: Retrofit
    private lateinit var entryInterface : EntryInterface
    private lateinit var intent : Intent
    private var pressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        retrofit = APIClient().getClient()!!

        entryInterface = retrofit.create(EntryInterface::class.java)

        binding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().contains(" ")) {
                    val filteredText: String = s.toString().replace(" ", "")
                    binding.emailEditText.setText(filteredText)
                    binding.emailEditText.setSelection(filteredText.length)
                }
            }
        })

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
                        EncryptSharedPreference(this@LoginActivity).getEncryptedSharedPreference()?.edit()?.putString("token", responseBodyToken)
                            ?.apply()

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

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}