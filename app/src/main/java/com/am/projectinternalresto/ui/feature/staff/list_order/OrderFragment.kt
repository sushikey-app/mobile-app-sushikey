package com.am.projectinternalresto.ui.feature.staff.list_order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.data.response.staff.order.ListOrderResponse
import com.am.projectinternalresto.databinding.FragmentOrderBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order.OrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Key.BUNDLE_ID_ORDER
import com.am.projectinternalresto.utils.Key.IS_PAID_ORDER
import com.am.projectinternalresto.utils.Key.IS_UNPAID_ORDER
import com.am.projectinternalresto.utils.Navigation.navigateFragment
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
                    // change status order (progress to done)
                    setupPostChangeStatusOrder(id)
                } else {
                    // payment for edit order
                    id.let { orderId ->
                        viewModel.getDetailOrder(token, orderId)
                            .observe(viewLifecycleOwner) { result ->
                                when (result.status) {
                                    Status.LOADING -> {}
                                    Status.SUCCESS -> {
                                        result.data?.data.let { orderDetails ->
                                            val orderSummary = orderDetails?.order?.map { item ->
                                                DummyModel.CartItem(
                                                    menuItem = DataItemMenu(
                                                        imageMenu = item?.menu?.image,
                                                        quota = item?.menu?.kuota,
                                                        noMenu = item?.menu?.nomorMenu,
                                                        nameMenu = item?.menu?.nama,
                                                        price = item?.menu?.harga,
                                                        idMenu = item?.menu?.id
                                                    ),
                                                    qty = item?.qty ?: 0,
                                                    selectedToppings = (item?.topping
                                                        ?: emptyList()) as List<ToppingItem>,
                                                    note = item?.note ?: ""
                                                )
                                            }?.let {
                                                DummyModel.OrderSummary(
                                                    orderId = orderDetails.payment?.id,
                                                    listCartItems = it,
                                                    typeOrder = orderDetails.payment?.typeOrder.toString(),
                                                    totalPurchase = orderDetails.payment?.totalPrice
                                                        ?: 0
                                                )
                                            }
                                            navigateFragment(
                                                Destination.ORDER_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
                                                findNavController(),
                                                Bundle().apply {
                                                    putString(Key.BUNDLE_ID_ORDER, orderDetails?.payment?.id.toString())
                                                    putParcelable(
                                                        Key.BUNDLE_DATA_ORDER_TO_PAYMENT,
                                                        orderSummary
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    Status.ERROR -> {
                                        Log.e("Check", "is error")
                                        showErrorSnackBar(requireView(), result.message.toString())
                                    }
                                }
                            }
                    }
                }
            }
            callbackOnClickToDetailOrderListener { id ->
                if (isPaid) {
                    navigateFragment(
                        Destination.ORDER_TO_DETAIL_ORDER,
                        findNavController(),
                        Bundle().apply {
                            putString(BUNDLE_ID_ORDER, id)
                        }
                    )
                } else {
                    id.let { orderId ->
                        viewModel.getDetailOrder(token, orderId)
                            .observe(viewLifecycleOwner) { result ->
                                when (result.status) {
                                    Status.LOADING -> {}
                                    Status.SUCCESS -> {
                                        result.data?.data.let { orderDetails ->
                                            val orderSummary = orderDetails?.order?.map { item ->
                                                DummyModel.CartItem(
                                                    menuItem = DataItemMenu(
                                                        imageMenu = item?.menu?.image,
                                                        quota = item?.menu?.kuota,
                                                        noMenu = item?.menu?.nomorMenu,
                                                        nameMenu = item?.menu?.nama,
                                                        price = item?.menu?.harga,
                                                        idMenu = item?.menu?.id
                                                    ),
                                                    qty = item?.qty ?: 0,
                                                    selectedToppings = (item?.topping
                                                        ?: emptyList()) as List<ToppingItem>,
                                                    note = item?.note ?: ""
                                                )
                                            }?.let {
                                                DummyModel.OrderSummary(
                                                    orderId = orderDetails.payment?.id,
                                                    listCartItems = it,
                                                    typeOrder = orderDetails.payment?.typeOrder.toString(),
                                                    totalPurchase = orderDetails.payment?.totalPrice
                                                        ?: 0
                                                )
                                            }
                                            navigateFragment(
                                                Destination.ORDER_TO_ORDER_MENU,
                                                findNavController(),
                                                Bundle().apply {
                                                    putString(Key.BUNDLE_ID_ORDER, orderDetails?.payment?.id.toString())
                                                    putParcelable(
                                                        Key.BUNDLE_DATA_ORDER_TO_EDIT,
                                                        orderSummary
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    Status.ERROR -> {
                                        Log.e("Check", "is error")
                                        showErrorSnackBar(requireView(), result.message.toString())
                                    }
                                }
                            }
                    }
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

    private fun setupPostChangeStatusOrder(idOrder: String) {
        viewModel.changeStatusOrder(token, idOrder).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    showSuccessSnackBar(requireView(), result.data?.message.toString())
                    setupGetDataOrder()
                }

                Status.ERROR -> {
                    showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }
}