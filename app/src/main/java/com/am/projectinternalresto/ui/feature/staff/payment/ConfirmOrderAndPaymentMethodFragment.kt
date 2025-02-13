package com.am.projectinternalresto.ui.feature.staff.payment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.body_params.OrderItemRequest
import com.am.projectinternalresto.data.body_params.OrderRequest
import com.am.projectinternalresto.data.body_params.PaymentRequest
import com.am.projectinternalresto.data.body_params.ToppingItemRequest
import com.am.projectinternalresto.data.dummy.DummyData.paymentOption
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.staff.order.PayResponse
import com.am.projectinternalresto.databinding.FragmentConfirmOrderAndPaymentMethodBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.cart.CartAdapter
import com.am.projectinternalresto.ui.adapter.payment.TotalPaymentOptionAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.widget.dialog_fragment.PaymentSuccessDialog
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Key.BUNDLE_CUSTOMER_NAME
import com.am.projectinternalresto.utils.Key.BUNDLE_DATA_ORDER_TO_PAYMENT
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import com.am.projectinternalresto.utils.setPriceWatcherUtils
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class ConfirmOrderAndPaymentMethodFragment : Fragment() {
    private var _binding: FragmentConfirmOrderAndPaymentMethodBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private var unformattedTotalPaid = 0
    private var paymentMethod = "TUNAI"
    private val dataOrderSummary: DummyModel.OrderSummary? by lazy {
        arguments?.getParcelable(
            BUNDLE_DATA_ORDER_TO_PAYMENT
        )
    }
    private val dataIdOrder: String? by lazy { arguments?.getString(Key.BUNDLE_ID_ORDER) }
    private val dataCustomerName: String? by lazy { arguments?.getString(BUNDLE_CUSTOMER_NAME) }
    private val bluetoothPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
//                printReceipt()
            } else {
                // Permissions not granted, show error message
                NotificationHandle.showErrorSnackBar(
                    requireView(),
                    "Izin Bluetooth diperlukan untuk mencetak struk."
                )
            }
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
        binding.cardPayment.buttonCancel.setOnClickListener {
//            findNavController().previousBackStackEntry?.savedStateHandle?.set("isClear", true)
            findNavController().popBackStack()
        }
        binding.cardPayment.buttonPayment.setOnClickListener {
            setupPostDataPayment()
        }
    }

    private fun checkBluetoothPermissionsAndPrint(data: PayResponse?) {
        if (hasBluetoothPermissions()) {
            printReceipt(data)
        } else {
            requestMultiplePermissions.launch(bluetoothPermissions)
        }
    }


    private fun hasBluetoothPermissions(): Boolean {
        return bluetoothPermissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setupView() {
        UiHandle.setupDisableHintForField(
            binding.cardPayment.edlTotalPayment
        )
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
        tabLayout.addTab(tabLayout.newTab().setText("TUNAI"))
        tabLayout.addTab(tabLayout.newTab().setText("TRANSFER"))
        tabLayout.addTab(tabLayout.newTab().setText("QRIS"))
        tabLayout.addTab(tabLayout.newTab().setText("GOJEK"))
        tabLayout.addTab(tabLayout.newTab().setText("GRAB"))

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                paymentMethod = tab.text.toString()
                val disabledPaymentMethods = setOf("QRIS", "TRANSFER", "GOJEK", "GRAB")
                binding.cardPayment.edtTotalPayment.apply {
                    isEnabled = paymentMethod !in disabledPaymentMethods
                }

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
                if (paymentMethod == "TUNAI") {
                    binding.cardPayment.edtTotalPayment.setText(it)
                }
            }
        }
        binding.cardPayment.recyclerViewOptionPay.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(context, 3)
        }
    }

    private fun setupPostDataPayment() {
        val specialPaymentMethods = setOf("QRIS", "TRANSFER", "GOJEK", "GRAB")
        val totalPayment = dataOrderSummary?.totalPurchase ?: 0 // Total yang harus dibayar

        if (unformattedTotalPaid == 0 && paymentMethod !in specialPaymentMethods) {
            NotificationHandle.showErrorSnackBar(requireView(), "Masukkan nominal pembayaran")
            return
        }

        if (unformattedTotalPaid < totalPayment && paymentMethod !in specialPaymentMethods) {
            NotificationHandle.showErrorSnackBar(
                requireView(),
                "Nominal pembayaran tidak mencukupi"
            )
            return
        }

        if (dataIdOrder != null) {
            viewModel.paymentOrder(token, dataIdOrder.toString(), collectDataPayment())
                .observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            ProgressHandle.setupVisibilityProgressBar(
                                binding.cardPayment.progressBar,
                                binding.cardPayment.textLoading,
                                true
                            )
                        }

                        Status.SUCCESS -> {
                            ProgressHandle.setupVisibilityProgressBar(
                                binding.cardPayment.progressBar,
                                binding.cardPayment.textLoading,
                                false
                            )
                            PaymentSuccessDialog.show(
                                childFragmentManager,
                                result.data
                            ) { shouldPrint ->
                                if (shouldPrint) {
                                    checkBluetoothPermissionsAndPrint(result.data)
                                } else {
                                    findNavController().navigate(
                                        R.id.nav_staff_order,
                                        null,
                                        NavOptions.Builder().setPopUpTo(R.id.nav_staff_menu, true)
                                            .build()
                                    )
                                }
                            }
                        }

                        Status.ERROR -> {
                            ProgressHandle.setupVisibilityProgressBar(
                                binding.cardPayment.progressBar,
                                binding.cardPayment.textLoading,
                                false
                            )
                            NotificationHandle.showErrorSnackBar(
                                requireView(),
                                result.message.toString()
                            )
                        }
                    }
                }
        } else {
            viewModel.payFromOrderContinuation(token, collectDataOrderAndPayment())
                .observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            ProgressHandle.setupVisibilityProgressBar(
                                binding.cardPayment.progressBar,
                                binding.cardPayment.textLoading,
                                true
                            )
                        }

                        Status.SUCCESS -> {
                            ProgressHandle.setupVisibilityProgressBar(
                                binding.cardPayment.progressBar,
                                binding.cardPayment.textLoading,
                                false
                            )

                            PaymentSuccessDialog.show(
                                childFragmentManager,
                                result.data
                            ) { shouldPrint ->
                                if (shouldPrint) {
                                    checkBluetoothPermissionsAndPrint(result.data)
                                } else {
                                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                        "isUpdated",
                                        true
                                    )
                                    findNavController().popBackStack()
                                }
                            }
                        }

                        Status.ERROR -> {
                            ProgressHandle.setupVisibilityProgressBar(
                                binding.cardPayment.progressBar,
                                binding.cardPayment.textLoading,
                                false
                            )
                            NotificationHandle.showErrorSnackBar(
                                requireView(),
                                result.message.toString()
                            )
                        }
                    }
                }
        }
    }

    private fun collectDataPayment(): PaymentRequest {
        return PaymentRequest(
            nameBuyer = dataCustomerName.toString(),
            methodPayment = paymentMethod,
            totalPaid = unformattedTotalPaid
        )
    }

    private fun collectDataOrderAndPayment(
    ): OrderRequest {
        val itemOrder = dataOrderSummary?.listCartItems?.map { items ->
            OrderItemRequest(menuId = items.menuItem.idMenu.toString(),
                qty = items.qty,
                note = items.note,
                topping = items.selectedToppings.map { topping -> ToppingItemRequest(topping.id.toString()) })
        } ?: emptyList()

        return OrderRequest(
            nameBuyer = dataCustomerName.toString(),
            methodPayment = paymentMethod,
            totalPaid = unformattedTotalPaid,
            order = itemOrder
        )
    }

    private fun printReceipt(orderResponse: PayResponse?) {
        try {
            // Cetak pertama
            printReceiptContent(orderResponse)
            // Jeda sebentar sebelum cetak kedua
            Thread.sleep(1000)
            // Cetak kedua
            printReceiptContent(orderResponse)

            NotificationHandle.showSuccessSnackBar(requireView(), "Struk berhasil dicetak")
        } catch (e: Exception) {
            e.printStackTrace()
            NotificationHandle.showErrorSnackBar(
                requireView(),
                "Gagal mencetak struk: ${e.message}"
            )
        }
    }

    // Memisahkan logika cetak ke fungsi terpisah
    private fun printReceiptContent(orderResponse: PayResponse?) {
        val items = mutableListOf<Triple<String, Int, Int>>()

        orderResponse?.data?.orderItems?.forEach { item ->
            val menuName = item?.nama.toString()
            val note = item?.note
            val toppings = item?.topping?.joinToString(", ") { it?.nama ?: "" }

            // Hitung total harga item termasuk topping
            val basePrice = item?.hargaPesanan ?: 0
            val toppingPrice = item?.topping?.sumOf { it?.harga ?: 0 } ?: 0
            val totalItemPrice = basePrice + toppingPrice
            val qty = item?.qty ?: 0

            var itemText = menuName
            if (toppings?.isNotEmpty() == true) {
                itemText += if (itemText.length + toppings.length + 4 <= 20) {
                    " ($toppings)"
                } else {
                    "\n  ($toppings)"
                }
            }

            if (note != null) {
                itemText += if (itemText.length + note.length + 4 <= 20) {
                    " ($note)"
                } else {
                    "\n  ($note)"
                }
            }

            items.add(Triple(itemText, totalItemPrice, qty))
        }

        val printerConnection = BluetoothPrintersConnections.selectFirstPaired()

        if (printerConnection == null) {
            throw Exception("Printer tidak ditemukan!")
        }

        val printer = EscPosPrinter(printerConnection, 203, 48f, 32)

        val bitmap = requireContext().resources.getDrawableForDensity(
            R.drawable.logo_sushi_key,
            DisplayMetrics.DENSITY_MEDIUM
        )?.toBitmap()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap!!, 300, 100, true)

        val logoHex = PrinterTextParserImg.bitmapToHexadecimalString(printer, resizedBitmap)

        var receiptText = "[C]<img>$logoHex</img>\n" +
                "[C]===============================\n" +
                "[L]No. Order: ${orderResponse?.data?.payment?.nomorOrder}\n" +
                "[L]Nama: ${orderResponse?.data?.payment?.namaPembeli}\n" +
                "[L]Nama Kasir: ${orderResponse?.data?.staffName}\n" +
                "[L]Lokasi Resto: ${orderResponse?.data?.locationName}\n" +
                "[C]===============================\n" +
                "[L]Qty Menu[R]Total\n" +
                "[C]-------------------------------\n"

        items.forEach { (nama, harga, qty) ->
            val totalHarga = harga * qty
            receiptText += "[L]$qty " + " $nama[R]${formatCurrency(totalHarga)}\n"
        }

        val specialPaymentMethods = setOf("QRIS", "TRANSFER", "GOJEK", "GRAB")

        val totalKeseluruhan = items.sumOf { it.second * it.third }

        val cash = if (orderResponse?.data?.payment?.metode in specialPaymentMethods) {
            formatCurrency(orderResponse?.data?.payment?.totalHarga ?: 0)
        } else {
            formatCurrency(orderResponse?.data?.payment?.uangDibayarkan ?: 0)
        }

        val cashBack = if (orderResponse?.data?.payment?.metode in specialPaymentMethods) {
            formatCurrency(0)
        } else {
            formatCurrency(
                orderResponse?.data?.payment?.uangDibayarkan?.minus(
                    orderResponse.data.payment.totalHarga ?: 0
                ) ?: 0
            )
        }

        receiptText += "[C]-------------------------------\n" +
                "[L]Total[R] ${formatCurrency(totalKeseluruhan)}\n" +
                "[L]${orderResponse?.data?.payment?.metode}[R] $cash\n" +
                "[L]Kembalian[R] $cashBack\n" +
                "[C]===============================\n" +
                "[L]Tanggal: ${Formatter.getCurrentDateAndTime()}\n" +
                "[L]Terima kasih!"

        printer.printFormattedText(receiptText)
    }
}