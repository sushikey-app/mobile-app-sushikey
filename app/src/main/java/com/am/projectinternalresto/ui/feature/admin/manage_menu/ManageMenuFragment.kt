package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.databinding.FragmentManageMenuBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_menu.ManageMenuAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.ProgressHandle
import org.koin.android.ext.android.inject

class ManageMenuFragment : Fragment() {
    private var _binding: FragmentManageMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var adapter: ManageMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageMenuBinding.inflate(inflater, container, false)
        setupGetDataFromApi()
        setupManageMenuAdapter()
        setupNavigation()
        return binding.root
    }

    private fun setupGetDataFromApi() {
        viewModel.getMenu(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupDeleteDataMenu(idMenu: String) {
        viewModel.deleteMenu(token, idMenu).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(null)
                setupGetDataFromApi()
            }
        }
    }

    private fun setupManageMenuAdapter() {
        adapter = ManageMenuAdapter().apply {
            callbackOnEditClickListener { menu ->
                Navigation.navigateFragment(
                    Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_MENU, menu) }
                )
            }
            callbackOnDeleteClickListener { idMenu -> setupDeleteDataMenu(idMenu) }
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

    private fun <T> handleApiStatus(
        result: Resource<T>,
        errorMessage: String,
        onSuccess: () -> Unit
    ) {
        when (result.status) {
            Status.LOADING -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageMenu.shimmerLayout,
                    true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageMenu.shimmerLayout,
                    false
                )
                onSuccess.invoke()
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageMenu.shimmerLayout,
                    false
                )
            }
        }
    }
}