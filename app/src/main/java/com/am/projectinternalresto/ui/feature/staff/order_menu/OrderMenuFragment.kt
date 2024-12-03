package com.am.projectinternalresto.ui.feature.staff.order_menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.databinding.FragmentOrderMenuBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.cart.CartAdapter
import com.am.projectinternalresto.ui.adapter.menu.MenuAdapter
import com.am.projectinternalresto.ui.feature.admin.manage_category.ManageCategoryViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertAddToCart
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Key.BUNDLE_DATA_ORDER_TO_PAYMENT
import com.am.projectinternalresto.utils.Key.BUNDLE_ID_ORDER
import com.am.projectinternalresto.utils.Navigation.navigateFragment
import com.am.projectinternalresto.utils.NotificationHandle.showErrorSnackBar
import com.am.projectinternalresto.utils.NotificationHandle.showSuccessSnackBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityProgressBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class OrderMenuFragment : Fragment() {
    private var _binding: FragmentOrderMenuBinding? = null
    private val binding
        get() = _binding ?: throw IllegalArgumentException("Tampilan tidak ditemukan")
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val categoryViewModel: ManageCategoryViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var cartAdapter: CartAdapter
    private lateinit var menuAdapter: MenuAdapter
    private val dataEditingOrderSummary: DummyModel.OrderSummary? by lazy {
        arguments?.getParcelable(
            Key.BUNDLE_DATA_ORDER_TO_EDIT
        )
    }
    private var editingOrderId: String? = null
    private val dataIdOrder: String? by lazy { arguments?.getString(Key.BUNDLE_ID_ORDER) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialUI()
        observeViewModel()
        setupGetDataMenuFromApi()
        setupGetDataCategory()

    }

    private fun setupInitialUI() {
        setupMenuRecyclerViews()
        setupCartRecyclerView()
        setupTabLayoutOrderType()
        handleEditOrderState()
        setupNavigation()
    }

    private fun setupMenuRecyclerViews() {
        menuAdapter = MenuAdapter().apply {
            callbackOnClickListener { dataMenu ->
                showAlertAddToCart(requireContext(), viewModel, dataMenu)
            }
        }
        binding.recyclerview.apply {
            adapter = menuAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun setupCartRecyclerView() {
        cartAdapter = CartAdapter { itemId, increment ->
            val success = viewModel.updateQuantity(itemId, increment)
            if (!success) {
                showErrorSnackBar(
                    requireView(),
                    "Maaf tidak dapat menambahkan menu ini. Dikarenakan stock sudah habis"
                )
            }
        }
        binding.cardChart.recyclerViewChart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupTabLayoutOrderType() {
        val tabLayout = binding.cardChart.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Offline"))
        tabLayout.addTab(tabLayout.newTab().setText("Online"))

        // Atur margin untuk setiap tab
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

    // setup ui when edit order
    private fun handleEditOrderState() {
        dataEditingOrderSummary?.let {
            cartAdapter.submitList(dataEditingOrderSummary?.listCartItems)
            viewModel.initializeCartWithExistingOrder(dataEditingOrderSummary!!)
            binding.cardChart.buttonSave.text = "Cancel Order"
        }

    }

    private fun setupGetDataCategory() {
        categoryViewModel.getCategoryMenu(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setupTabLayoutCategory(result.data?.data as List<DataItemCategory>)
                }

                Status.ERROR -> {
                    showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupTabLayoutCategory(categories: List<DataItemCategory>) {
        val tabLayout = binding.tabLayout

        tabLayout.addTab(tabLayout.newTab().setText("All"))

        for (category in categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category.nameCategory))
        }

        // Atur margin untuk setiap tab
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupNavigation() {
        binding.cardChart.buttonPay.setOnClickListener { navigateToPayment() }
        binding.cardChart.buttonSave.setOnClickListener {
            if (dataEditingOrderSummary != null) {
                Log.e("Check", "Is Cancel")
                setupCancelOrder()
            } else {
                Log.e("Check", "Is Edit")
                setupPostDataSaveOrderToApi()
            }
        }
    }

    private fun navigateToPayment() {
        val orderSummary = DummyModel.OrderSummary(
            orderId = editingOrderId,
            listCartItems = viewModel.cartItems.value ?: emptyList(),
            typeOrder = viewModel.orderType.value.toString(),
            totalPurchase = viewModel.totalPurchase.value ?: 0
        )
        navigateFragment(
            Destination.ORDER_MENU_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
            findNavController(),
            Bundle().apply {
                putString(BUNDLE_ID_ORDER, dataIdOrder)
                putParcelable(BUNDLE_DATA_ORDER_TO_PAYMENT, orderSummary)
            }
        )

        clearDataChart()
    }

    private fun setupGetDataMenuFromApi() {
        viewModel.getDataMenuOrder(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, true)
                }

                Status.SUCCESS -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, false)
                    result.data?.data?.let { menuAdapter.submitList(it) }
                    result.data?.data?.forEach {
                        viewModel.updateItemStock(
                            it?.idMenu.toString(),
                            it?.quota ?: 0
                        )
                    }
                }

                Status.ERROR -> {
                    setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, false)
                    showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupPostDataSaveOrderToApi() {
        viewModel.saveDataOrder(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                clearDataChart()
                setupGetDataMenuFromApi()
                showSuccessSnackBar(requireView(), result.data?.message.toString())
            }
        }
    }

    private fun setupCancelOrder() {
        Log.e("token", "Token : $token | ${dataEditingOrderSummary?.orderId}" )
        viewModel.cancelOrder(token, dataEditingOrderSummary?.orderId.toString())
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result, result.message.toString()) {
                    clearDataChart()
                    setupGetDataMenuFromApi()
                    showSuccessSnackBar(requireView(), result.data?.message.toString())
                }
            }
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
            val isCartEmpty = items.isNullOrEmpty()
            binding.cardChart.buttonPay.isEnabled = !isCartEmpty
            binding.cardChart.buttonSave.isEnabled = !isCartEmpty

            binding.cardChart.apply {
                if (buttonPay.isEnabled) {
                    buttonPay.alpha = 1F
                    buttonSave.alpha = 1F
                } else {
                    buttonPay.alpha = 0.4F
                    buttonSave.alpha = 0.5F
                }
            }
        }

        viewModel.itemStock.observe(viewLifecycleOwner) { stock ->
            menuAdapter.updateStock(stock)
        }
        viewModel.totalPurchase.observe(viewLifecycleOwner) { total ->
            binding.cardChart.textTotal.text = Formatter.formatCurrency(total)
        }
        viewModel.orderType.observe(viewLifecycleOwner) { type ->
            updateOrderTypeUI(type)
        }
    }

    private fun updateOrderTypeUI(type: String) {
        val tabLayout = binding.cardChart.tabLayout
        when (type) {
            "Offline" -> tabLayout.getTabAt(0)?.select()
            "Online" -> tabLayout.getTabAt(1)?.select()
        }
    }

    private fun clearDataChart() {
        viewModel.clearCart()
        cartAdapter.submitList(emptyList())
        binding.cardChart.textTotal.text = Formatter.formatCurrency(0)
    }


    private fun <T> handleApiStatus(
        result: Resource<T>,
        errorMessage: String,
        onSuccess: () -> Unit
    ) {
        when (result.status) {
            Status.LOADING -> {
                setupVisibilityProgressBar(
                    binding.cardChart.progressBar,
                    binding.cardChart.textLoading,
                    true
                )
            }

            Status.SUCCESS -> {
                setupVisibilityProgressBar(
                    binding.cardChart.progressBar,
                    binding.cardChart.textLoading,
                    false
                )
                onSuccess.invoke()

            }

            Status.ERROR -> {
                setupVisibilityProgressBar(
                    binding.cardChart.progressBar,
                    binding.cardChart.textLoading,
                    false
                )
                showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}