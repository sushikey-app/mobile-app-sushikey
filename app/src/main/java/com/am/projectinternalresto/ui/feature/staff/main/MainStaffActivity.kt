package com.am.projectinternalresto.ui.feature.staff.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.ActivityMainStaffBinding
import com.am.projectinternalresto.ui.widget.alert.AlertDialog
import com.am.projectinternalresto.utils.UiHandler

class MainStaffActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainStaffBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navigationHostStaff) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationRails()
    }

    private fun setupNavigationRails() {
        binding.navigationRailsStaff.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_staff_logout -> {
                    AlertDialog.showAlertLogout(this@MainStaffActivity)
                    true
                }
                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }

        }
    }
}