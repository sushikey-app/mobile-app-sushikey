package com.am.projectinternalresto.ui.feature.admin.manage_staff

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
import com.am.projectinternalresto.ui.adapter.manage_staff.ManageStaffAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertDeleteData
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class ManageStaffFragment : Fragment() {
    private var _binding: FragmentManageEmployeesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageStaffViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var adapter: ManageStaffAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageEmployeesBinding.inflate(inflater, container, false)
        setupView()
        setupNavigation()
        setupAdapterStaff()
        return binding.root
    }

    private fun setupView() {
        setupGetDataStaffFromApi()
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataStaffFromApi() }
        UiHandle.setupDisplayDataFromSearchOrGet(
            editLayout= binding.search.edlSearch,
            editText = binding.search.edtSearch,
            onSearchDisplayData = { keyword -> setupSearchStaff(keyword) },
            onDisplayDataDefault = { setupGetDataStaffFromApi() }
        )
    }

    private fun setupNavigation() {
        binding.cardManageAdmin.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF, findNavController()
            )
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupGetDataStaffFromApi()
        }
    }

    private fun setupGetDataStaffFromApi() {
        viewModel.getAllDataStaff(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupDeleteDataStaff(idStaff: String) {
        viewModel.deleteStaff(token, idStaff).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(null)
                setupGetDataStaffFromApi()
            }
        }
    }

    private fun setupSearchStaff(keyword: String) {
        viewModel.searchStaff(token, keyword).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupAdapterStaff() {
        adapter = ManageStaffAdapter().apply {
            callbackOnEditClickListener { dataStaff ->
                Navigation.navigateFragment(
                    Destination.MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_STAFF, dataStaff) }
                )
            }
            callbackOnDeleteClickListener { idStaff ->
                showAlertDeleteData(requireContext(), "Pegawai", "pegawai") {
                    setupDeleteDataStaff(idStaff)
                }
            }
        }
        binding.cardManageAdmin.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
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
                    binding.cardManageAdmin.shimmerLayout,
                    true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageAdmin.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                onSuccess.invoke()
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