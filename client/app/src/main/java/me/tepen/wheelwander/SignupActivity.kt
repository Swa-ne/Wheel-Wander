package me.tepen.wheelwander

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.tepen.wheelwander.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}