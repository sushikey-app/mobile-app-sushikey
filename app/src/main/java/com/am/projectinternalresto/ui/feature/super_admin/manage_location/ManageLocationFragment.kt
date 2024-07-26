package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentManageLocationBinding
import com.am.projectinternalresto.ui.adapter.manage_location.ManageLocationAdapter
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation

class ManageLocationFragment : Fragment() {
    private var _binding: FragmentManageLocationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageLocationBinding.inflate(inflater, container, false)
        setupAdapter()
        setupNavigation()
        return binding.root
    }

    private fun setupAdapter() {
        val adapter = ManageLocationAdapter()
        adapter.submitList(DummyData.dummyDataCardLocation)
        binding.cardManageLocation.recyclerViewContentTableLocation.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
        adapter.let {
            it.onClickButtonEdit = {
                Toast.makeText(requireContext(), "IsEdit", Toast.LENGTH_SHORT).show()
            }
            it.onClickButtonDelete = {
                Toast.makeText(requireContext(), "IsEdit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigation() {
        binding.cardManageLocation.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
                findNavController()
            )
        }
    }

}