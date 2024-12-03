package com.am.projectinternalresto.ui.feature.super_admin.manage_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.databinding.FragmentManageEmployeesBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_admin.ManageAdminAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class ManageAdminFragment : Fragment() {
    private var _binding: FragmentManageEmployeesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageAdminViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var adapter: ManageAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageEmployeesBinding.inflate(inflater, container, false)
        setupView()
        setupAdapterManageAdminAndSuperAdmin()
        setupNavigation()
        return binding.root
    }

    private fun setupView() {
        UiHandle.setupClearTextForField(binding.search.edtSearch)
        UiHandle.setupDisableHintForField(binding.search.edlSearch)
        setupGetDataFromApi()
        UiHandle.setupDisplayDataFromSearchOrGet(
            editText = binding.search.edtSearch,
            onSearchDisplayData = { keyword -> setupSearchAdminAndSuperAdminByName(keyword) },
            onDisplayDataDefault = { setupGetDataFromApi() }
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupGetDataFromApi()
        }
    }

    private fun setupGetDataFromApi() {
        viewModel.getAllDataAdminAndSuperAdmin(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupSearchAdminAndSuperAdminByName(keyword: String) {
        viewModel.searchAdminAndSuperAdmin(token, keyword).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupDeleteDataAdminOrSuperAdmin(id: String) {
        viewModel.deleteAdminOrSuperAdmin(token, id).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(null)
                setupGetDataFromApi()
            }
        }
    }

    private fun setupNavigation() {
        binding.cardManageAdmin.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN, findNavController()
            )
        }
    }

    private fun setupAdapterManageAdminAndSuperAdmin() {
        adapter = ManageAdminAdapter().apply {
            callbackOnEditClickListener { user ->
                Navigation.navigateFragment(Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_ADMIN_OR_SUPER_ADMIN, user) })
            }
            callbackOnDeleteClickListener { id -> setupDeleteDataAdminOrSuperAdmin(id) }
        }

        binding.cardManageAdmin.recyclerView.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    private fun <T> handleApiStatus(
        result: Resource<T>,
        errorMessage: String,
        onSuccess: () -> Unit
    ) {
        when (result.status) {
            Status.LOADING -> {
                adapter.submitList(emptyList())
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageAdmin.shimmerLayout,
                    true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageAdmin.shimmerLayout,
                    false
                )
                onSuccess.invoke()
                binding.swipeRefreshLayout.isRefreshing = false
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageAdmin.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                NotificationHandle.showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }
}