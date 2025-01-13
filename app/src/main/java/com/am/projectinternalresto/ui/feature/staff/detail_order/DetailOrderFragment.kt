package com.am.projectinternalresto.ui.feature.staff.detail_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.staff.order.DataItemOrder
import com.am.projectinternalresto.databinding.FragmentDetailOrderBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order.DetailOrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.NotificationHandle
import org.koin.android.ext.android.inject

class DetailOrderFragment : Fragment() {
    private var _binding: FragmentDetailOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private val idOrder: String by lazy { arguments?.getString(Key.BUNDLE_ID_ORDER).toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailOrderBinding.inflate(inflater, container, false)
        setupNavigation()
        setupGetDetailOrder()
        return binding.root
    }

    private fun setupNavigation() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonChangeStatus.setOnClickListener {
            setupPostChangeStatusOrder(idOrder)
        }
    }

    private fun setupView(data: DataItemOrder?) {
        if (data?.payment?.statusOrder == "Proses") {
            binding.buttonChangeStatus.visibility = View.VISIBLE
        } else {
            binding.buttonChangeStatus.visibility = View.GONE
        }
        binding.textValueBuyerName.text = data?.payment?.buyerName
        binding.textValueStaffName.text = data?.staffName.toString()
        binding.textValueStatusOrder.text = data?.payment?.statusOrder.toString()
        binding.textValueDateOrders.text = Formatter.getCurrentDate()
        binding.layoutOrderItems.textValueSubTotal.text =
            Formatter.formatCurrency(data?.payment?.totalPrice ?: 0)
        binding.layoutOrderItems.textValuePPN.text = Formatter.formatCurrency(0)
        binding.layoutOrderItems.textValueTotal.text =
            Formatter.formatCurrency(data?.payment?.totalPrice ?: 0)
    }

    private fun setupGetDetailOrder() {
        viewModel.getDetailOrder(token, idOrder).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setupAdapter(result.data?.data)
                    setupView(result.data?.data)
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapter(data: DataItemOrder?) {
        val adapter = DetailOrderAdapter().apply { submitList(data?.order) }
        binding.layoutOrderItems.recyclerConfirmOrder.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupPostChangeStatusOrder(idOrder: String) {
        viewModel.changeStatusOrder(token, idOrder).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    NotificationHandle.showSuccessSnackBar(requireView(), "Loading...")
                }

                Status.SUCCESS -> {
                    findNavController().popBackStack()
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }
}