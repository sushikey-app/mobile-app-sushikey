package com.am.projectinternalresto.ui.feature.super_admin.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.ActivityMainSuperAdminBinding
import com.am.projectinternalresto.ui.widget.alert.AlertDialog

class MainSuperAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainSuperAdminBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navigationHostSuperAdmin) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSuperAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    /*fungsi ini berfungsi untuk mengatur navigasi di tampilan menu pada user super admin*/
    private fun setupBottomNavigation() {
        binding.navigationRailsSuperAdmin.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_super_admin_logout -> {
                    AlertDialog.showAlertLogout(this@MainSuperAdminActivity)
                    true
                }

                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
    }


}