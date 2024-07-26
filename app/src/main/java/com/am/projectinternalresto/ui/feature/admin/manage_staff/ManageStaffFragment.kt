package com.am.projectinternalresto.ui.feature.admin.manage_staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentManageEmployeesBinding
import com.am.projectinternalresto.ui.adapter.manage_admin.ManageAdminAdapter
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation

class ManageStaffFragment : Fragment() {
    private var _binding: FragmentManageEmployeesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageEmployeesBinding.inflate(inflater, container, false)
        setupNavigation()
        setupAdapter()
        return binding.root
    }

    private fun setupNavigation() {
        binding.cardManageEmployees.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF,
                findNavController()
            )
        }
    }

    private fun setupAdapter() {
        val adapter = ManageAdminAdapter()
        adapter.submitList(DummyData.dummyDataManageAdmin)
        binding.cardManageEmployees.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}