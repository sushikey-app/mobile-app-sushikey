package com.am.projectinternalresto.ui.feature.staff.cancel_order_staff

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.am.projectinternalresto.data.response.super_admin.cancel_order.ListCancelResponse
import com.am.projectinternalresto.databinding.FragmentCancelOrderBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order.CancelOrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertCancelOrder
import com.am.projectinternalresto.ui.widget.alert.showAlertReasonCancelOrder
import com.am.projectinternalresto.ui.widget.dialog_fragment.DetailReportDialogFragment
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import org.koin.android.ext.android.inject

class CancelOrderStaffFragment : Fragment() {
    private var _binding: FragmentCancelOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCancelOrderBinding.inflate(inflater, container, false)
        setupGetCancelOrder()
        return binding.root
    }

    private fun setupGetCancelOrder() {
        viewModel.getCancelOrderStaff(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.shimmerLayout,
                        true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.shimmerLayout,
                        false
                    )
                    setupAdapter(result.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.shimmerLayout,
                        false
                    )
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapter(data: ListCancelResponse?) {
        val adapter = CancelOrderAdapter(isStaff = true).apply {
            submitList(data?.data)
            callbackClickDetailOrder { id ->
                DetailReportDialogFragment.show(childFragmentManager, id)
            }
            callbackClickYes { id ->
                showAlertCancelOrder(requireContext()) {
                    setupPostData(id, "Ya", null)
                }
            }
            callbackClickNo { id ->
                showAlertReasonCancelOrder(requireContext()) { reason ->
                    setupPostData(id, "Tidak", reason)
                }
            }
        }

        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setupPostData(id: String, status: String, reason: String?) {
        viewModel.confirmCancelOrder(token, id, status, reason)
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        NotificationHandle.showSuccessSnackBar(
                            requireView(),
                            result.data?.message.toString()
                        )
                        setupGetCancelOrder()
                    }

                    Status.ERROR -> {
                        NotificationHandle.showErrorSnackBar(
                            requireView(),
                            result.message.toString()
                        )
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}