package me.tepen.wheelwander.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import me.tepen.wheelwander.R
import me.tepen.wheelwander.network.APIClient
import me.tepen.wheelwander.utils.EncryptSharedPreference
import me.tepen.wheelwander.interfaces.EntryInterface
import me.tepen.wheelwander.models.LoginResult
import me.tepen.wheelwander.databinding.ActivityMainBinding
import me.tepen.wheelwander.fragments.HomeFragment
import me.tepen.wheelwander.fragments.Page2Fragment
import me.tepen.wheelwander.fragments.Page3Fragment
import me.tepen.wheelwander.fragments.MarketFragment
import me.tepen.wheelwander.fragments.ProfileFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var intent : Intent
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val page2Fragment = Page2Fragment()
        val page3Fragment = Page3Fragment()
        val profileFragment = ProfileFragment()
        val marketFragment = MarketFragment()

        setCurrentFragment(homeFragment)
        binding.marketButton.setOnClickListener{
            setCurrentFragment(marketFragment)
        }
        binding.bottomNavigation.setOnItemSelectedListener  {
            when(it.itemId){
                R.id.homeFragment ->setCurrentFragment(homeFragment)
                R.id.page2Fragment ->setCurrentFragment(page2Fragment)
                R.id.messageFragment ->setCurrentFragment(page3Fragment)
                R.id.profileFragment ->setCurrentFragment(profileFragment)

            }
            true
        }
//        TODO: use of back button
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
}