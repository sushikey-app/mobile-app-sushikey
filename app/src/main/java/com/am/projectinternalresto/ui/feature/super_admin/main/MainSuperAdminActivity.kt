package com.am.projectinternalresto.ui.feature.super_admin.main

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.ActivityMainSuperAdminBinding
import com.am.projectinternalresto.ui.widget.alert.showAlertLogout

class MainSuperAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainSuperAdminBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navigationHostSuperAdmin) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSuperAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation(){
        val isTablet = resources.getBoolean(R.bool.isTablet)
        if (isTablet){
            binding.bottomNavigation.visibility = View.GONE
            setupRailsNavigation()
        }else{
            binding.navigationRailsSuperAdmin.visibility = View.GONE
            setupBottomNavigation()
        }
    }

    /*fungsi ini berfungsi untuk mengatur navigasi di tampilan menu pada user super admin*/
    private fun setupRailsNavigation() {
        binding.navigationRailsSuperAdmin.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_super_admin_logout -> {
                    showAlertLogout(this@MainSuperAdminActivity)
                    true
                }
                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
    }


    private fun setupBottomNavigation(){
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_navigation_super_admin)
        val menu = popupMenu.menu
        binding.bottomNavigation.setupWithNavController( menu, navController)
    }
}