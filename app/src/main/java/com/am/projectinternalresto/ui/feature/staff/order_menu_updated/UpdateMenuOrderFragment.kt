package com.am.projectinternalresto.ui.feature.staff.order_menu_updated

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.data.response.staff.order.DataItemOrder
import com.am.projectinternalresto.databinding.FragmentOrderMenuBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.cart.CartAdapter
import com.am.projectinternalresto.ui.adapter.menu.MenuAdapter
import com.am.projectinternalresto.ui.feature.admin.manage_category.ManageCategoryViewModel
import com.am.projectinternalresto.ui.feature.admin.manage_menu.ManageMenuViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertAddToCart
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Key.BUNDLE_ID_ORDER
import com.am.projectinternalresto.utils.Navigation.navigateFragment
import com.am.projectinternalresto.utils.NotificationHandle.showErrorSnackBar
import com.am.projectinternalresto.utils.NotificationHandle.showSuccessSnackBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityProgressBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout
import com.am.projectinternalresto.utils.UiHandle
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class UpdateMenuOrderFragment : Fragment() {
    private var _binding: FragmentOrderMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private lateinit var menuAdapter: MenuAdapter

    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val categoryViewModel: ManageCategoryViewModel by inject()
    private val menuViewModel: ManageMenuViewModel by inject()

    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private val idOrder: String by lazy { arguments?.getString(Key.BUNDLE_ID_ORDER).toString() }
    private var idPayment: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchOrderDetail(idOrder)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderMenuBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        setupInitialUI()
        observeViewModel()
        setupGetDataMenuFromApi()
        setupGetDataCategory()
    }

    private fun setupInitialUI() {
        setupMenuRecyclerViews()
        setupCartRecyclerView()
        setupNavigation()
        UiHandle.setupDisplayDataFromSearchOrGet(editLayout = binding.search.edlSearch,
            editText = binding.search.edtSearch,
            onSearchDisplayData = { keyword -> setupSearchMenu(keyword) },
            onDisplayDataDefault = { setupGetDataMenuFromApi() })
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
            viewModel.updateQuantity(itemId, increment)
        }
        binding.cardChart.recyclerViewChart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupNavigation() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupGetDataMenuFromApi()
            setupGetDataCategory()
        }
        binding.cardChart.buttonPay.setOnClickListener {
            updateOrderMenu(idPayment, false)
        }
        binding.cardChart.buttonSave.setOnClickListener {
            updateOrderMenu(idPayment, true)
        }
    }

    private fun setupSearchMenu(keyword: String) {
        viewModel.searchMenuOrder(token, keyword).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> handleVisibilityShimmer(true)
                Status.SUCCESS -> handleSuccessGetMenu(data = result.data?.data)
                Status.ERROR -> handleError(result.message.toString())
            }
        }
    }

    private fun setupTabLayoutCategory(categories: List<DataItemCategory?>?) =
        with(binding.tabLayout) {
            removeAllTabs()

            // Tambahkan tab "All" dan kategori lainnya
            addTab(newTab().setText("All"))
            categories?.forEach { category ->
                addTab(newTab().setText(category?.nameCategory))
            }

            // Atur margin antar tab
            (getChildAt(0) as? ViewGroup)?.let { tabStrip ->
                for (i in 0 until tabStrip.childCount) {
                    (tabStrip.getChildAt(i).layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                        setMargins(5, 0, 5, 0)
                        tabStrip.getChildAt(i).layoutParams = this
                    }
                }
            }

            // Listener saat tab dipilih
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    menuAdapter.submitList(emptyList())
                    if (tab.position == 0) {
                        setupGetDataMenuFromApi()
                    } else {
                        categories?.getOrNull(tab.position - 1)?.let {
                            setupFilterMenuByCategory(it.id)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }


    private fun setupGetDataMenuFromApi() {
        viewModel.getDataMenuOrder(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> handleVisibilityShimmer(true)
                Status.SUCCESS -> handleSuccessGetMenu(result.data?.data)
                Status.ERROR -> handleError(result.message.toString())
            }
        }
    }

    private fun setupGetDataCategory() {
        categoryViewModel.getCategoryMenu(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    setupTabLayoutCategory(result.data?.data)
                }

                Status.ERROR -> handleError(result.message.toString())
            }
        }
    }

    private fun fetchOrderDetail(orderId: String) {
        viewModel.getDetailOrder(token, orderId).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> result.data?.data?.let { handleSuccessOrderDetail(it) }
                Status.ERROR -> handleError(result.message.toString())
            }
        }
    }

    private fun setupFilterMenuByCategory(idMenu: String) {
        menuViewModel.filterMenuPesanByCategory(token, idMenu)
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> handleVisibilityShimmer(true)
                    Status.SUCCESS -> handleSuccessGetMenu(result.data?.data)
                    Status.ERROR -> handleError(result.message.toString())
                }
            }
    }

    private fun updateOrderMenu(idPay: String, isSave: Boolean) {
        viewModel.updateOrder(token, idPay, viewModel.mappingDataResultUpdateMenu())
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result, result.message.toString()) {
                    if (isSave) findNavController().popBackStack() else
                        handleNavigationToPayment()
                    clearDataChart()
                    showSuccessSnackBar(requireView(), result.data?.message.toString())
                }
            }
    }

    private fun handleNavigationToPayment() {
        viewModel.getDetailOrder(token, idOrder).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> handlePaymentProcess(result.data?.data)
                Status.ERROR -> {}
            }
        }
    }

    private fun handlePaymentProcess(orderDetails: DataItemOrder?) {
        if (orderDetails == null) {
            showErrorSnackBar(requireView(), "No order details found")
            return
        }

        val orderSummary = createOrderSummary(orderDetails)
        Log.e("CHECK_NAVIGATE", "INI FUNCTION handle")
        navigateToPaymentMethod(idOrder, orderSummary)
    }

    private fun createOrderSummary(orderDetails: DataItemOrder): DummyModel.OrderSummary? {
        val cartItems = orderDetails.order?.mapNotNull { item ->
            item?.let {
                DummyModel.CartItem(
                    menuItem = DataItemMenu(
                        imageMenu = it.menu?.image,
                        quota = it.menu?.kuota,
                        noMenu = it.menu?.nomorMenu,
                        nameMenu = it.menu?.nama,
                        price = it.menu?.discPrice ?: it.menu?.harga,
                        idMenu = it.menu?.id
                    ),
                    qty = it.qty ?: 0,
                    selectedToppings = (it.topping ?: emptyList()),
                    note = it.note ?: ""
                )
            }
        }

        return cartItems?.let {
            DummyModel.OrderSummary(
                orderId = orderDetails.payment?.id,
                listCartItems = it,
                totalPurchase = orderDetails.payment?.totalPrice ?: 0,
                totalDisc = 0
            )
        }
    }

    private fun navigateToPaymentMethod(
        idOrder: String, orderSummary: DummyModel.OrderSummary?
    ) {
        Log.e("CHECK_NAVIGATE", "INI FUNCTION NAVIGATE")
        if (orderSummary == null) {
            showErrorSnackBar(requireView(), "Failed to create order summary")
            return
        }

        navigateFragment(Destination.ORDER_MENU_UPDATED_PAYMENT_METHOD,
            findNavController(),
            Bundle().apply {
                putString(BUNDLE_ID_ORDER, idOrder)
                putParcelable(Key.BUNDLE_DATA_ORDER_TO_PAYMENT, orderSummary)
            })
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
            val isCartEmpty = items.isNullOrEmpty()
            val buttonStateAlpha = if (isCartEmpty) 0.4F else 1F
            binding.cardChart.apply {
                buttonPay.isEnabled = !isCartEmpty
                buttonSave.isEnabled = !isCartEmpty
                buttonPay.alpha = buttonStateAlpha
                buttonSave.alpha = if (isCartEmpty) 0.5F else 1F
            }
        }

        viewModel.totalPurchase.observe(viewLifecycleOwner) { total ->
            binding.cardChart.textTotal.text = Formatter.formatCurrency(total)
        }
    }

    private fun handleSuccessOrderDetail(orderDetails: DataItemOrder) {
        idPayment = orderDetails.payment?.id.toString()
        val cartItems = orderDetails.order?.mapNotNull { item ->
            item?.let {
                DummyModel.CartItem(
                    idOrder = it.id.orEmpty(),
                    qty = it.qty ?: 0,
                    note = it.note.orEmpty(),
                    selectedToppings = (it.topping ?: emptyList<ToppingItem>()),
                    menuItem = DataItemMenu(
                        idMenu = it.menu?.id.orEmpty(),
                        nameMenu = it.menu?.nama.orEmpty(),
                        price = it.menu?.discPrice ?: it.menu?.harga ?: 0,
                        noMenu = it.menu?.nomorMenu.orEmpty(),
                        quota = it.menu?.kuota,
                        imageMenu = it.menu?.image
                    )
                )
            }
        } ?: emptyList()

        cartAdapter.submitList(cartItems)

        viewModel.initializeCartWithExistingOrder(
            DummyModel.OrderSummary(
                orderId = orderDetails.payment?.id.orEmpty(),
                listCartItems = cartItems,
                totalPurchase = orderDetails.payment?.totalPrice ?: 0,
                totalDisc = 0
            )
        )
        binding.cardChart.buttonSave.text = getString(R.string.save_order)
    }

    private fun handleSuccessGetMenu(data: List<DataItemMenu?>?) {
        handleVisibilityShimmer(false)
        binding.swipeRefreshLayout.isRefreshing = false
        menuAdapter.submitList(data)
    }

    private fun handleError(errorMessage: String) {
        handleVisibilityShimmer(false)
        showErrorSnackBar(requireView(), errorMessage)
    }

    private fun handleVisibilityShimmer(isLoading: Boolean) {
        setupVisibilityShimmerLoadingInLinearLayout(binding.shimmerLayout, isLoading)
    }

    private fun clearDataChart() {
        viewModel.clearCart()
        cartAdapter.submitList(emptyList())
        binding.cardChart.textTotal.text = Formatter.formatCurrency(0)
    }


    private fun <T> handleApiStatus(
        result: Resource<T>, errorMessage: String, onSuccess: () -> Unit
    ) {
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
                onSuccess.invoke()

            }

            Status.ERROR -> {
                setupVisibilityProgressBar(
                    binding.cardChart.progressBar, binding.cardChart.textLoading, false
                )
                showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }
}