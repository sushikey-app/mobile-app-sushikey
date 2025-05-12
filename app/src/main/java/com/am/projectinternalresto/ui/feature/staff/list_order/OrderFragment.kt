package com.am.projectinternalresto.ui.feature.staff.list_order

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.staff.order.DataItemOrder
import com.am.projectinternalresto.data.response.staff.order.ListOrderResponse
import com.am.projectinternalresto.databinding.FragmentOrderBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order.OrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertDeleteData
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Key.BUNDLE_ID_ORDER
import com.am.projectinternalresto.utils.Key.IS_PAID_ORDER
import com.am.projectinternalresto.utils.Key.IS_UNPAID_ORDER
import com.am.projectinternalresto.utils.Navigation.navigateFragment
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.NotificationHandle.showErrorSnackBar
import com.am.projectinternalresto.utils.NotificationHandle.showSuccessSnackBar
import com.am.projectinternalresto.utils.ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import org.koin.android.ext.android.inject

class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by inject()
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
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
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        setupView()
        setupNavigation()
        setupGetDataOrder()
        return binding.root
    }

    private fun setupNavigation() {
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataOrder() }
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
                    setupDetailOrderProcessing(id, false)
                }
            }
            callbackOnClickToDetailOrderListener { id ->
                if (isPaid) {
                    navigateFragment(
                        Destination.ORDER_TO_DETAIL_ORDER,
                        findNavController(),
                        Bundle().apply {
                            putString(BUNDLE_ID_ORDER, id)
                        })
                } else {
                    navigateFragment(Destination.ORDER_TO_ORDER_MENU_UPDATED,
                        findNavController(),
                        Bundle().apply { putString(BUNDLE_ID_ORDER, id) })
                }
            }
            callbackOnclickCancelListener { id ->
                showAlertDeleteData(
                    requireContext(),
                    "Batalkan Pesanan",
                    "Apakah Anda yakin ingin membatalkan pesanan ini"
                ) {
                    setupCancelOrder(id)
                }
            }

            callbackOnclickPrint { id ->
                setupDetailOrderProcessing(id, true)
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


    private fun setupDetailOrderProcessing(orderId: String, isPrintReceipt: Boolean = false) {
        viewModel.getDetailOrder(token, orderId).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (isPrintReceipt) {
                        // Handle receipt printing
                        checkBluetoothPermissionsAndPrint(result.data?.data)
                    } else {
                        // navigate to payment
                        handlePaymentProcess(orderId, result.data?.data)
                    }
                }
                Status.ERROR -> showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    private fun handlePaymentProcess(idOrder: String, orderDetails: DataItemOrder?) {
        if (orderDetails == null) {
            showErrorSnackBar(requireView(), "No order details found")
            return
        }
        val orderSummary = createOrderSummary(orderDetails)
        navigateToPaymentMethod(idOrder, orderSummary)
    }

    private fun hasBluetoothPermissions(): Boolean {
        return bluetoothPermissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkBluetoothPermissionsAndPrint(data: DataItemOrder?) {
        if (hasBluetoothPermissions()) {
            printReceipt(data)
        } else {
            requestMultiplePermissions.launch(bluetoothPermissions)
        }
    }


    private fun printReceipt(orderResponse: DataItemOrder?) {
        try {
            // Cetak pertama
            printReceiptContent(orderResponse)
            showSuccessSnackBar(requireView(), "Struk berhasil dicetak")
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorSnackBar(
                requireView(), "Gagal mencetak struk: ${e.message}"
            )
        }
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
        if (orderSummary == null) {
            showErrorSnackBar(requireView(), "Failed to create order summary")
            return
        }

        navigateFragment(Destination.ORDER_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
            findNavController(),
            Bundle().apply {
                putString(BUNDLE_ID_ORDER, idOrder)
                putParcelable(Key.BUNDLE_DATA_ORDER_TO_PAYMENT, orderSummary)
            })
    }


    // Memisahkan logika cetak ke fungsi terpisah
    private fun printReceiptContent(orderResponse: DataItemOrder?) {
        val items = mutableListOf<Triple<String, Int, Int>>()

        orderResponse?.order?.forEach { item ->
            val menuName = item?.menu?.nama.toString()
            val note = item?.note
            val toppings = item?.topping?.joinToString(", ") { it?.nama ?: "" }

            // Hitung total harga item termasuk topping
            val totalItemPrice = item?.hargaPesanan ?: 0
            val qty = item?.qty ?: 0

            var itemText = menuName
            if (toppings?.isNotEmpty() == true) {
                itemText += if (itemText.length + toppings.length + 4 <= 20) {
                    " ($toppings)"
                } else {
                    "\n ($toppings)"
                }
            }

            if (note != null) {
                itemText += if (itemText.length + note.length + 4 <= 20) {
                    " ($note)"
                } else {
                    "\n($note)"
                }
            }

            items.add(Triple(itemText, totalItemPrice, qty))
        }

        val printerConnection = BluetoothPrintersConnections.selectFirstPaired() ?: throw Exception(
            "Printer tidak ditemukan!"
        )

        val printer = EscPosPrinter(printerConnection, 203, 48f, 32)

        val bitmap = requireContext().resources.getDrawableForDensity(
            R.drawable.logo_sushi_key, DisplayMetrics.DENSITY_MEDIUM
        )?.toBitmap()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap!!, 300, 100, true)

        val logoHex = PrinterTextParserImg.bitmapToHexadecimalString(printer, resizedBitmap)

        var receiptText = "[C]<img>$logoHex</img>\n" +
                "[C]===============================\n" +
                "[L]No. Order: ${orderResponse?.payment?.noOrder}\n" +
                "[L]Nama: ${orderResponse?.payment?.buyerName}\n" +
                "[L]Nama Kasir: ${orderResponse?.staffName}\n" +
                "[L]Metode Pembayaran: ${orderResponse?.payment?.paymentMethod}\n" +
                "[L]Lokasi Resto: ${orderResponse?.locationName}\n" +
                "[C]===============================\n" +
                "[L]Qty Menu[R]Total\n" +
                "[C]-------------------------------\n"


        val maxLineChar = 30 // panjang baris printer thermal

        items.forEach { (nama, harga, qty) ->
            val lines = nama.split("\n")
            val firstLine = "$qty ${lines[0]}"

            val priceStr = formatCurrency(harga)
            val spaceAvailable = maxLineChar - priceStr.length

            // Jika nama (firstLine) muat dalam 1 baris bersama harga
            if (firstLine.length <= spaceAvailable) {
                receiptText += "[L]${firstLine.padEnd(spaceAvailable)}[R]$priceStr\n"
            } else {
                // Nama terlalu panjang, cetak terpisah
                receiptText += "[L]$firstLine\n"
                receiptText += "[R]$priceStr\n"
            }

            // Tambahkan baris-baris tambahan (catatan, topping)
            if (lines.size > 1) {
                for (i in 1 until lines.size) {
                    receiptText += "[L]  ${lines[i]}\n"
                }
            }
        }


        val specialPaymentMethods = setOf("QRIS", "TRANSFER", "GOJEK", "GRAB")
        val typePayment =
            if (orderResponse?.payment?.paymentMethod !in specialPaymentMethods) "Uang Dibayarkan" else "Jumlah Dibayarkan"
        val cashBack = if (orderResponse?.payment?.paymentMethod in specialPaymentMethods) {
            formatCurrency(0)
        } else {
            formatCurrency(
                orderResponse?.payment?.moneyPaid?.minus(
                    orderResponse.payment.totalPrice ?: 0
                ) ?: 0
            )
        }

        receiptText += "[C]-------------------------------\n" +
                "[L]SubTotal[R] ${formatCurrency(orderResponse?.payment?.subtotal ?: 0)}\n" +
                "[L]Diskon[R] ${formatCurrency(orderResponse?.payment?.disc ?: 0)}\n" +
                "[L]Total[R] ${formatCurrency(orderResponse?.payment?.totalPrice ?: 0)}\n" +
                "[L]$typePayment[R] ${formatCurrency(orderResponse?.payment?.moneyPaid ?: 0)}\n" +
                "[L]Kembalian[R] $cashBack\n" +
                "[C]===============================\n" +
                "[L]Tanggal: ${Formatter.getCurrentDateAndTime()}\n" +
                "[L]Terima kasih!"
        printer.printFormattedText(receiptText)
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
                binding.swipeRefreshLayout.isRefreshing = false
                setupAdapter(result.data, isPaid)
            }

            Status.ERROR -> {
                setupVisibilityShimmerLoadingInLinearLayout(shimmerLayout, false)
                showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    private fun setupCancelOrder(id: String) {
        viewModel.cancelOrder(token, id).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    showSuccessSnackBar(requireView(), result.data?.message.toString())
                    clearAdapters()
                    setupGetDataOrder()
                }

                Status.ERROR -> {
                    showErrorSnackBar(requireView(), result.message.toString())
                }
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
                    clearAdapters()
                    setupGetDataOrder()
                }

                Status.ERROR -> {
                    showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun clearAdapters() {
        (binding.layoutPaidOrders.recyclerView.adapter as? OrderAdapter)?.submitList(emptyList())
        (binding.layoutUnpaidOrders.recyclerView.adapter as? OrderAdapter)?.submitList(emptyList())
    }
}