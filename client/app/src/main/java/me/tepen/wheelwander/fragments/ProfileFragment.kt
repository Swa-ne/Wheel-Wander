package me.tepen.wheelwander.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.tepen.wheelwander.R
import me.tepen.wheelwander.activities.LoginActivity
import me.tepen.wheelwander.databinding.FragmentProfileBinding
import me.tepen.wheelwander.utils.EncryptSharedPreference

class ProfileFragment : Fragment() {

    private lateinit var intent : Intent
    private lateinit var binding : FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.logoutButton.setOnClickListener{
            activity?.let { it1 ->
                EncryptSharedPreference(it1).getEncryptedSharedPreference()?.edit()?.clear()
                    ?.apply()
            }
            intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}