package com.am.projectinternalresto.ui.feature.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentDashboardBinding
import com.am.projectinternalresto.ui.adapter.dahboard.MenuFavoriteAdapter
import com.google.android.material.tabs.TabLayout

class DashboardAdminFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupCardMenuFavorite()
        setupTabLayout()
        return binding.root
    }

    private fun setupCardMenuFavorite() {
        val adapter = MenuFavoriteAdapter()
        adapter.submitList(DummyData.dummyCardMenuFavorite)
        binding.cardMenuFavorite.apply {
            recyclerViewFavoriteMenu.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewFavoriteMenu.adapter = adapter
        }
    }


    private fun setupTabLayout() {
        val tabLayout = binding.cardMenuFavorite.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Makanan"))
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"))

        for (i in 0 until tabLayout.tabCount){
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Toast.makeText(requireContext(), tab.text.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}