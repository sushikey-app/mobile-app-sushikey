package com.am.projectinternalresto.ui.feature.staff.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.body_params.OrderItemRequest
import com.am.projectinternalresto.data.body_params.OrderRequest
import com.am.projectinternalresto.data.body_params.ToppingItemRequest
import com.am.projectinternalresto.data.dummy.DummyData.paymentOption
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.databinding.FragmentConfirmOrderAndPaymentMethodBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.cart.CartAdapter
import com.am.projectinternalresto.ui.adapter.payment.TotalPaymentOptionAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.am.projectinternalresto.utils.Key.BUNDLE_DATA_ORDER_TO_PAYMENT
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.setPriceWatcherUtils
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class ConfirmOrderAndPaymentMethodFragment : Fragment() {
    private var _binding: FragmentConfirmOrderAndPaymentMethodBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private var unformattedTotalPaid = 0
    private var paymentMethod = "Offline"
    private val dataOrderSummary: DummyModel.OrderSummary? by lazy {
        arguments?.getParcelable(
            BUNDLE_DATA_ORDER_TO_PAYMENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmOrderAndPaymentMethodBinding.inflate(inflater, container, false)
        setupView()
        setupNavigation()
        setupTabLayoutTypePayment()
        setupItemOrderAdapter()
        setupPaymentAdapter()
        return binding.root
    }

    private fun setupNavigation() {
        binding.cardPayment.buttonPayment.setOnClickListener {
            viewModel.orderMenu(token, createOrderRequest()).observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        findNavController().popBackStack()
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
        binding.cardPayment.buttonCancel.setOnClickListener { findNavController() }
    }

    private fun setupView() {
        dataOrderSummary?.let { orderSummary ->
            binding.cardConfirmOrder.apply {
                textValueSubTotal.text = formatCurrency(orderSummary.totalPurchase)
                textValuePPN.text = formatCurrency(0)
                textValueTotal.text = formatCurrency(orderSummary.totalPurchase)
            }
        }
        binding.cardPayment.apply {
            edtTotalPayment.setPriceWatcherUtils { total ->
                unformattedTotalPaid = total
            }
        }
    }

    private fun setupTabLayoutTypePayment() {
        val tabLayout = binding.cardPayment.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Cash"))
        tabLayout.addTab(tabLayout.newTab().setText("Qris"))

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                paymentMethod = tab.text.toString()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupItemOrderAdapter() {
        val orderItemsAdapter = CartAdapter { itemId, increment ->
            viewModel.updateQuantity(itemId, increment)
        }
        orderItemsAdapter.submitList(dataOrderSummary?.listCartItems)
        binding.cardConfirmOrder.recyclerConfirmOrder.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderItemsAdapter
        }
    }

    private fun setupPaymentAdapter() {
        val adapter = TotalPaymentOptionAdapter().apply {
            submitList(paymentOption)
            callbackOnClickListener {
                binding.cardPayment.edtTotalPayment.setText(it)
            }
        }
        binding.cardPayment.recyclerViewOptionPay.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(context, 3)
        }
    }

    fun createOrderRequest(
    ): OrderRequest {
        val itemOrder = dataOrderSummary?.listCartItems?.map { items ->
            OrderItemRequest(menuId = items.menuItem.idMenu.toString(),
                qty = items.qty,
                note = items.note,
                topping = items.selectedToppings.map { topping -> ToppingItemRequest(topping.id.toString()) })
        } ?: emptyList()

        return OrderRequest(
            nameBuyer = binding.cardPayment.edtNameBuyer.text.toString(),
            typeOrder = dataOrderSummary?.typeOrder ?: "",
            methodPayment = paymentMethod,
            totalPaid = unformattedTotalPaid,
            order = itemOrder
        )
    }
}