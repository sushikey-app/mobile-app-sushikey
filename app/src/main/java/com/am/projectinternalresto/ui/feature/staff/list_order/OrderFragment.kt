package com.am.projectinternalresto.ui.feature.staff.list_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.staff.order.ListOrderResponse
import com.am.projectinternalresto.databinding.FragmentOrderBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order.OrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.utils.Key.IS_PAID_ORDER
import com.am.projectinternalresto.utils.Key.IS_UNPAID_ORDER
import com.am.projectinternalresto.utils.NotificationHandle.showErrorSnackBar
import com.am.projectinternalresto.utils.NotificationHandle.showSuccessSnackBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout
import org.koin.android.ext.android.inject

class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by inject()
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        setupView()
        setupGetDataOrder()
        return binding.root
    }

    private fun setupView() {
        binding.layoutPaidOrders.textInformationOrder.text =
            getString(R.string.information_order_in_progress)
        binding.layoutUnpaidOrders.textInformationOrder.text =
            getString(R.string.information_unpaid_order)
    }

    private fun setupGetDataOrder() {
        viewModel.listOrder(token, IS_PAID_ORDER).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, true)
        }
        viewModel.listOrder(token, IS_UNPAID_ORDER).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, false)
        }
    }

    private fun setupAdapter(data: ListOrderResponse?, isPaid: Boolean) {
        val orderAdapter = OrderAdapter().apply {
            submitList(data?.data)
            callbackOnclickChangeStatusListener { id ->
                if (isPaid) {
                    showSuccessSnackBar(requireView(), "Is Paid : $id")
                } else {
                    showSuccessSnackBar(requireView(), "Is Unpaid : $id")
                }
            }
            callbackOnClickToDetailOrderListener { id ->
                if (isPaid) {
                    showErrorSnackBar(requireView(), "Is Paid : $id")
                } else {
                    showErrorSnackBar(requireView(), "Is Unpaid : $id")
                }
            }
        }

        if (isPaid) {
            binding.layoutPaidOrders.recyclerView.let {
                it.adapter = orderAdapter
                it.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        } else {
            binding.layoutUnpaidOrders.recyclerView.let {
                it.adapter = orderAdapter
                it.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }


    private fun handleApiStatus(result: Resource<ListOrderResponse?>, isPaid: Boolean) {
        val shimmerLayout =
            if (isPaid) binding.layoutPaidOrders.shimmerLayout else binding.layoutUnpaidOrders.shimmerLayout
        when (result.status) {
            Status.LOADING -> {
                setupVisibilityShimmerLoadingInLinearLayout(shimmerLayout, true)
            }

            Status.SUCCESS -> {
                setupVisibilityShimmerLoadingInLinearLayout(shimmerLayout, false)
                setupAdapter(result.data, isPaid)
            }

            Status.ERROR -> {
                setupVisibilityShimmerLoadingInLinearLayout(shimmerLayout, false)
                showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }
}