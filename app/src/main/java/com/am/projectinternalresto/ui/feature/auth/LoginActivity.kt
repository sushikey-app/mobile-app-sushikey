package com.am.projectinternalresto.ui.feature.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.am.projectinternalresto.databinding.ActivityLoginBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.feature.admin.main.MainAdminActivity
import com.am.projectinternalresto.ui.feature.staff.main.MainStaffActivity
import com.am.projectinternalresto.ui.feature.super_admin.main.MainSuperAdminActivity
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import com.am.projectinternalresto.utils.goToActivity
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("Checklogin", "${viewModel.isLoginUser()} | ${viewModel.isLoginExpired()}")
        if (!viewModel.isLoginUser()) {
            setupView()
        } else if (viewModel.isLoginExpired()) {
            NotificationHandle.showErrorSnackBar(
                binding.root,
                "Sesi Anda telah habis. Silakan login kembali."
            )
            setupView()
        } else {
            handleNavigationByRole(viewModel.getUserRole())
        }
        setupNavigation()

    }

    private fun setupView() {
        UiHandle.setupDisableHintForField(
            binding.cardLogin.edlUsername,
            binding.cardLogin.edlPassword
        )
    }

    private fun setupNavigation() {
        binding.cardLogin.buttonLogin.setOnClickListener {
            UiHandle.setupHideKeyboard(it)
            login()
        }

    }

    private fun login() {
        val username = binding.cardLogin.edtUsername.text.toString()
        val password = binding.cardLogin.edtPassword.text.toString()

        when {
            username.isEmpty() -> {
                NotificationHandle.showErrorSnackBar(
                    binding.root,
                    "Login failed. Username tidak boleh kosong"
                )
                return
            }

            password.isEmpty() -> {
                NotificationHandle.showErrorSnackBar(
                    binding.root,
                    "Login failed. Password tidak boleh kosong"
                )
                return
            }

            else -> viewModel.login(username, password).observe(this@LoginActivity) { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.cardLogin.buttonLogin.apply {
                            isEnabled = false
                            alpha = 0.5f
                        }
                        ProgressHandle.setupVisibilityProgressBar(
                            binding.cardLogin.progressBar, binding.cardLogin.textLoading, true
                        )
                    }

                    Status.SUCCESS -> {
                        ProgressHandle.setupVisibilityProgressBar(
                            binding.cardLogin.progressBar, binding.cardLogin.textLoading, false
                        )
                        val token = resource.data?.token.toString()
                        val role = resource.data?.data?.role.toString()
                        viewModel.saveTokenUser(token, role)
                        viewModel.saveUserRole(role)
                        UiHandle.setupClearTextForField(
                            binding.cardLogin.edtUsername,
                            binding.cardLogin.edtPassword
                        )
                        handleNavigationByRole(role)
                    }

                    Status.ERROR -> {
                        ProgressHandle.setupVisibilityProgressBar(
                            binding.cardLogin.progressBar, binding.cardLogin.textLoading, false
                        )
                        NotificationHandle.showErrorSnackBar(
                            binding.root,
                            resource.message.toString()
                        )
                    }
                }
            }
        }
    }

    private fun handleNavigationByRole(role: String?) {
        when (role) {
            KEY_SUPER_ADMIN -> goToActivity(MainSuperAdminActivity::class.java, true)
            KEY_ADMIN -> goToActivity(MainAdminActivity::class.java, true)
            KEY_STAFF -> goToActivity(MainStaffActivity::class.java, true)
            else -> NotificationHandle.showErrorSnackBar(binding.root, "$role tidak ditemukan")
        }
    }

    companion object {
        const val KEY_SUPER_ADMIN = "Super Admin"
        const val KEY_ADMIN = "Admin"
        const val KEY_STAFF = "Pegawai"
    }
}