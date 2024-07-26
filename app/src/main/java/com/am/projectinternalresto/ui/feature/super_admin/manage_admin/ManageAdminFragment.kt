package com.am.projectinternalresto.ui.feature.super_admin.manage_admin

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
import com.am.projectinternalresto.utils.UiHandler

class ManageAdminFragment : Fragment() {
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
                Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN,
                findNavController()
            )
        }
    }

    private fun setupAdapter() {
        val adapter = ManageAdminAdapter().apply {
            submitList(DummyData.dummyDataManageAdmin)
            callbackOnEditClickListener {
                Navigation.navigateFragment(
                    Destination.MANAGE_ADMIN_TO_DETAIL_MANAGE_ADMIN,
                    findNavController()
                )
            }
            callbackOnDeleteClickListener {
                UiHandler.toastSuccessMessage(requireContext(), "Berhasil Menghapus data pegawai")
            }
        }

        binding.cardManageEmployees.recyclerView.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }
}