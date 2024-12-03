package com.am.projectinternalresto.ui.feature.staff.order_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.am.projectinternalresto.data.response.staff.order.ListOrderResponse
import com.am.projectinternalresto.data.response.staff.riwayat_order.DataItemHistory
import com.am.projectinternalresto.databinding.FragmentOrderHistoryBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order_history.HistoryOrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation.navigateFragment
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout
import org.koin.android.ext.android.inject

class OrderHistoryFragment : Fragment() {
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        setupGetDataHistoryOrder()
        return binding.root
    }

    private fun setupGetDataHistoryOrder() {
        viewModel.getHistoryOrder(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, true)
                }

                Status.SUCCESS -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, false)
                    setupAdapter(result.data?.data)
                }

                Status.ERROR -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, false)
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapter(data: DataItemHistory?) {
        val dataList = listOfNotNull(data?.pesananHariIni, data?.pesananHariSebelumnya).flatten()
        val adapter = HistoryOrderAdapter().apply {
            submitList(dataList)
            callbackClickDetailOrder { idOrder ->
                navigateFragment(Destination.HISTORY_ORDER_TO_DETAIL_HISTORY_ORDER,
                    findNavController(),
                    Bundle().apply { putString(Key.BUNDLE_ID_ORDER, idOrder) }
                )
            }
        }

        binding.recyclerView.let {
            it.adapter =adapter
            it.layoutManager = GridLayoutManager(context, 2)
        }
    }
}