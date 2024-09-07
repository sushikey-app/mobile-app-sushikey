package com.am.projectinternalresto.ui.feature.staff.order_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.menu.MenuResponse
import com.am.projectinternalresto.databinding.FragmentOrderMenuBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.cart.CartAdapter
import com.am.projectinternalresto.ui.adapter.menu.MenuAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.dialog.showAlertAddToCart
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Key.BUNDLE_DATA_ORDER_TO_PAYMENT
import com.am.projectinternalresto.utils.Navigation.navigateFragment
import com.am.projectinternalresto.utils.NotificationHandle.showErrorSnackBar
import com.am.projectinternalresto.utils.NotificationHandle.showSuccessSnackBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityProgressBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class OrderMenuFragment : Fragment() {
    private var _binding: FragmentOrderMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderMenuBinding.inflate(inflater, container, false)
        setupGetDataMenuFromApi()
        setupNavigation()
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.cardChart.textTotal.text = Formatter.formatCurrency(0)
        setupTabLayoutCategory()
        setupTabLayoutOrderType()
    }

    private fun setupNavigation() {
        binding.cardChart.buttonSave.setOnClickListener { setupPostDataSaveOrderToApi() }
        binding.cardChart.buttonPay.setOnClickListener {
            val orderSummary = DummyModel.OrderSummary(
                listCartItems = viewModel.cartItems.value ?: emptyList(),
                typeOrder = viewModel.orderType.value.toString(),
                totalPurchase = viewModel.totalPurchase.value ?: 0
            )
            navigateFragment(Destination.ORDER_MENU_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
                findNavController(),
                Bundle().apply { putParcelable(BUNDLE_DATA_ORDER_TO_PAYMENT, orderSummary) }
            )
        }
    }

    private fun setupPostDataSaveOrderToApi() {
        viewModel.saveDataOrder(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    setupVisibilityProgressBar(
                        binding.cardChart.progressBar, binding.cardChart.textLoading, true
                    )
                }

                Status.SUCCESS -> {
                    setupVisibilityProgressBar(
                        binding.cardChart.progressBar, binding.cardChart.textLoading, false
                    )
                    clearDataChart()
                    setupGetDataMenuFromApi()
                    showSuccessSnackBar(
                        requireView(),
                        result.data?.message.toString()
                    )
                }

                Status.ERROR -> {
                    setupVisibilityProgressBar(
                        binding.cardChart.progressBar, binding.cardChart.textLoading, false
                    )
                    showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupGetDataMenuFromApi() {
        viewModel.getDataMenuOrder(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, true)
                }

                Status.SUCCESS -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, false)
                    setupAdapter(result.data)
                }

                Status.ERROR -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, false)
                    showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapter(data: MenuResponse?) {
        val menuAdapter = MenuAdapter().apply {
            submitList(data?.data)
            callbackOnClickListener { dataMenu ->
                showAlertAddToCart(requireContext(), viewModel, dataMenu)
            }
        }
        cartAdapter = CartAdapter { itemId, increment ->
            viewModel.updateQuantity(itemId, increment)
        }
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
        }
        viewModel.totalPurchase.observe(viewLifecycleOwner) { total ->
            binding.cardChart.textTotal.text = Formatter.formatCurrency(total)
        }
        binding.cardChart.recyclerViewChart.let {
            it.adapter = cartAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.recyclerview.let {
            it.adapter = menuAdapter
            it.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }


    private fun setupTabLayoutCategory() {
        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Makanan"))
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"))

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Toast.makeText(requireContext(), tab.text.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupTabLayoutOrderType() {
        val tabLayout = binding.cardChart.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Offline"))
        tabLayout.addTab(tabLayout.newTab().setText("Online"))

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.setTypeOrder(tab.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun clearDataChart() {
        viewModel.clearCart()
        cartAdapter.submitList(emptyList())
        binding.cardChart.textTotal.text = Formatter.formatCurrency(0)
    }

}