package me.tepen.wheelwander.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.tepen.wheelwander.R
import me.tepen.wheelwander.network.APIClient
import me.tepen.wheelwander.utils.EncryptSharedPreference
import me.tepen.wheelwander.interfaces.EntryInterface
import me.tepen.wheelwander.models.LoginResult
import me.tepen.wheelwander.databinding.ActivityMainBinding
import me.tepen.wheelwander.fragments.HomeFragment
import me.tepen.wheelwander.fragments.MessagesFragment
import me.tepen.wheelwander.fragments.MarketFragment
import me.tepen.wheelwander.fragments.HistoryFragment
import me.tepen.wheelwander.fragments.ProfileFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var intent : Intent
    private lateinit var binding : ActivityMainBinding
    private var pressedTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val messagesFragment = MessagesFragment()
        val marketFragment = MarketFragment()
        val profileFragment = ProfileFragment()
        val historyFragment = HistoryFragment()

        setCurrentFragment(homeFragment)
        binding.marketButton.setOnClickListener{
            setCurrentFragment(marketFragment)
            binding.bottomNavigation.menu.setGroupCheckable(0, false, true);
        }
        binding.bottomNavigation.setOnItemSelectedListener  {
            binding.bottomNavigation.menu.setGroupCheckable(0, true, true);
            when(it.itemId){
                R.id.homeFragment ->setCurrentFragment(homeFragment)
                R.id.messagesFragment ->setCurrentFragment(messagesFragment)
                R.id.historyFragment ->setCurrentFragment(historyFragment)
                R.id.profileFragment ->setCurrentFragment(profileFragment)

            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView,fragment)
            commit()
        }
    override fun onStart() {
        super.onStart()
        var token : String? = EncryptSharedPreference(this@MainActivity).getEncryptedSharedPreference()?.getString("token", null)
        if (token == null) goToLogin()

        val retrofit = APIClient().getClient()!!
        val entryInterface = retrofit.create(EntryInterface::class.java)
        var call : Call<LoginResult> = entryInterface.executeCheckCurrentUser(token)
        call.enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {

                if(response.isSuccessful) {
                    var responseBodyMessage : String? = response.body()?.message
                    if(responseBodyMessage != "valid"){
                        goToLogin()
                    }
                } else {
                    goToLogin()
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                println("SwaneErr: $t$call")
                goToLogin()
            }
        })
    }

    private fun goToLogin(){
        intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
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