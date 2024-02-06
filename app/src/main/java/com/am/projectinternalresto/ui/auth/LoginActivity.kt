package com.am.projectinternalresto.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.am.projectinternalresto.ui.main.MainActivity
import com.am.projectinternalresto.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardLogin.buttonLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}