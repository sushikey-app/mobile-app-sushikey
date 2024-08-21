package com.am.projectinternalresto.ui.feature.admin.manage_staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.admin.manage_staff.StaffResponse
import com.am.projectinternalresto.databinding.FragmentManageEmployeesBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_staff.ManageStaffAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import org.koin.android.ext.android.inject

class ManageStaffFragment : Fragment() {
    private var _binding: FragmentManageEmployeesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageStaffViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageEmployeesBinding.inflate(inflater, container, false)
        setupNavigation()
        setupGetDataStaffFromApi()
        return binding.root
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
            when (result.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageAdmin.shimmerLayout, true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageAdmin.shimmerLayout, false
                    )
                    setupAdapterStaff(result.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageAdmin.shimmerLayout, false
                    )
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupDeleteDataStaff(idStaff: String) {
        viewModel.deleteStaff(token, idStaff).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setupGetDataStaffFromApi()
                    NotificationHandle.showSuccessSnackBar(
                        requireView(),
                        result.data?.message.toString()
                    )
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapterStaff(data: StaffResponse?) {
        val adapter = ManageStaffAdapter().apply {
            submitList(data?.data)
            callbackOnEditClickListener { dataStaff ->
                Navigation.navigateFragment(
                    Destination.MANAGE_STAFF_TO_ADD_OR_UPDATE_STAFF,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_STAFF, dataStaff) }
                )
            }
            callbackOnDeleteClickListener { idStaff ->
                setupDeleteDataStaff(idStaff)
            }
        }
        binding.cardManageAdmin.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}