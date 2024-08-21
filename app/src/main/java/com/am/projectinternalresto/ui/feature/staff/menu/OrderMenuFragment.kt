package com.am.projectinternalresto.ui.feature.staff.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentOrderMenuBinding
import com.am.projectinternalresto.ui.adapter.manage_location.SelectLocationAdapter
import com.am.projectinternalresto.ui.adapter.menu.MenuAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

class OrderMenuFragment : Fragment() {
    private var _binding: FragmentOrderMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderMenuBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupAdapter()
        setupTabLayout()
        binding.cardMenu.textMenuFavorite.text = getString(R.string.manage_menu)
    }

    private fun setupAdapter() {
        val adapter = MenuAdapter().apply {
            submitList(DummyData.dataDummyMenu)
            callbackAddToCart = {
                showAlertAddToCart()
            }
        }
        binding.cardMenu.recyclerViewFavoriteMenu.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupTabLayout() {
        val tabLayout = binding.cardMenu.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Makanan"))
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"))

        for (i in 0 until tabLayout.tabCount) {
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

    private fun showAlertAddToCart() {
        val builder = MaterialAlertDialogBuilder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.custom_layout_dialog_topping_and_note, null)
        val buttonSave = view.findViewById<Button>(R.id.buttonSaveDialogAddToCart)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTopping)
        val buttonClose = view.findViewById(R.id.buttonCloseDialog) as ImageView
        builder.setView(view)
        val adapter = SelectLocationAdapter()
        adapter.updateData(DummyData.dummyDataSelectLocation)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        buttonClose.setOnClickListener {
            builder.dismiss()
        }
        buttonSave.setOnClickListener {
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}