package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.admin.menu.MenuResponse
import com.am.projectinternalresto.databinding.FragmentManageMenuBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_menu.ManageMenuAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import org.koin.android.ext.android.inject

class ManageMenuFragment : Fragment() {
    private var _binding: FragmentManageMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageMenuBinding.inflate(inflater, container, false)
        setupGetDataFromApi()
        setupNavigation()
        return binding.root
    }

    private fun setupGetDataFromApi() {
        viewModel.getMenu(token).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageMenu.shimmerLayout,
                        true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageMenu.shimmerLayout,
                        false
                    )
                    setupAdapter(resource.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageMenu.shimmerLayout,
                        false
                    )
                }
            }
        }
    }

    private fun setupAdapter(data: MenuResponse?) {
        val adapter = ManageMenuAdapter().apply {
            submitList(data?.data)
            callbackOnEditClickListener { menu ->

                Navigation.navigateFragment(
                    Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_MENU, menu) }
                )
            }
            callbackOnDeleteClickListener {
                NotificationHandle.showSuccessSnackBar(requireView(), "Berhasil menghapus menu")
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