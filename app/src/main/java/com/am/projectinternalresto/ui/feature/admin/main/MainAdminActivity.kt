package com.am.projectinternalresto.ui.feature.admin.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.ActivityMainAdminBinding
import com.am.projectinternalresto.ui.widget.alert.AlertDialog

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navigationHostAdmin) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationRails()

    }

    private fun setupNavigationRails() {
        binding.navigationRailsAdmin.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_admin_logout -> {
                    AlertDialog.showAlertLogout(this@MainAdminActivity)
                    true
                }

                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
    }
}
