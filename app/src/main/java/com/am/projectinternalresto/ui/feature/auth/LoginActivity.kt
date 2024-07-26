package com.am.projectinternalresto.ui.feature.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.am.projectinternalresto.databinding.ActivityLoginBinding
import com.am.projectinternalresto.ui.feature.admin.main.MainAdminActivity
import com.am.projectinternalresto.ui.feature.staff.main.MainStaffActivity
import com.am.projectinternalresto.ui.feature.super_admin.main.MainSuperAdminActivity
import com.am.projectinternalresto.utils.Navigation.goToActivity
import com.am.projectinternalresto.utils.UiHandler

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupView()
    }

    private fun setupView() {
        UiHandler.setHintBehavior(binding.cardLogin.edlUsername, binding.cardLogin.edlPassword)
    }

    private fun setupNavigation() {
        binding.cardLogin.apply {
            val username = edtUsername.text
            val password = edtPassword.text
            buttonLogin.setOnClickListener {
                login(username.toString(), password.toString())
            }

        }
    }

    private fun login(username: String, password: String) {
        val usernameSuperAdmin = "superadmin"
        val passwordSuperAdmin = "superadmin"

        val usernameAdmin = "admin"
        val passwordAdmin = "admin"

        val usernameStaff = "staff"
        val passwordStaff = "staff"

        if (username == usernameSuperAdmin || password == passwordSuperAdmin) {
            goToActivity(MainSuperAdminActivity())
        } else if (username == usernameAdmin || password == passwordAdmin) {
            goToActivity(MainAdminActivity())
        } else if (username == usernameStaff || password == passwordStaff) {
            goToActivity(MainStaffActivity())
        } else {
            UiHandler.toastErrorMessage(
                this@LoginActivity,
                "Username or password not found"
            )
        }
    }
}