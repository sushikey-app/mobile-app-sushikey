package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentManageMenuBinding
import com.am.projectinternalresto.ui.adapter.manage_menu.ManageMenuAdapter
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.UiHandler

class ManageMenuFragment : Fragment() {
    private var _binding: FragmentManageMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageMenuBinding.inflate(inflater, container, false)
        setupNavigation()
        setupAdapter()
        return binding.root
    }

    private fun setupAdapter() {
        val adapter = ManageMenuAdapter().apply {
            submitList(DummyData.dataDummyMenu)
            callbackOnClickEdit = {
                Navigation.navigateFragment(
                    Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
                    findNavController()
                )
            }
            callBackOnClickDelete = {
                UiHandler.toastSuccessMessage(requireContext(), "Berhasil menghapus menu")
            }
        }

        binding.cardManageMenu.recyclerViewContentTableLocation.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupNavigation() {
        binding.cardManageMenu.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
                findNavController()
            )
        }
    }
}