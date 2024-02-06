package com.am.projectinternalresto.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationRails.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
    }
}
