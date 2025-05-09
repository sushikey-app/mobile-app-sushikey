package com.am.projectinternalresto.ui.feature.staff.list_order

import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.model.DummyModel
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.data.response.staff.order.DataItemOrder
import com.am.projectinternalresto.data.response.staff.order.ListOrderResponse
import com.am.projectinternalresto.data.response.staff.order.OrderResponse
import com.am.projectinternalresto.databinding.FragmentOrderBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.order.OrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
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
                                                        price = item?.menu?.discPrice ?: item?.menu?.harga,
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
                                                    totalPurchase = orderDetails.payment?.totalPrice ?: 0,
                                                    //TODO :: CHECK INI
                                                    totalDisc = 0
                                                )
                                            }
                                            navigateFragment(Destination.ORDER_TO_CONFIRM_ORDER_AND_PAYMENT_METHOD,
                                                findNavController(),
                                                Bundle().apply {
                                                    putString(
                                                        BUNDLE_ID_ORDER,
                                                        orderDetails?.payment?.id.toString()
                                                    )
                                                    putParcelable(
                                                        Key.BUNDLE_DATA_ORDER_TO_PAYMENT,
                                                        orderSummary
                                                    )
                                                })
                                        }
                                    }

                                    Status.ERROR -> {
                                        showErrorSnackBar(requireView(), result.message.toString())
                                    }
                                }
                            }
                    }
                }
            }
            callbackOnClickToDetailOrderListener { id ->
                if (isPaid) {
                    navigateFragment(Destination.ORDER_TO_DETAIL_ORDER,
                        findNavController(),
                        Bundle().apply {
                            putString(BUNDLE_ID_ORDER, id)
                        })
                } else {
                    setupGetDetail(false, id)
                }
            }
            callbackOnclickCancelListener { id ->
                setupCancelOrder(id)
            }

            callbackOnclickPrint { id ->
                setupGetDetail(true, id)
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

    private fun setupGetDetail(printStuck: Boolean, orderId: String) {
        viewModel.getDetailOrder(token, orderId).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> handleSuccess(printStuck, result.data)
                Status.ERROR -> showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    // TODO :: CHECK FUNCTION GET DETAIL AND PRINT
    private fun handleSuccess(detailForPrint: Boolean, orderResponse: OrderResponse?) {
        if (detailForPrint)
            printReceipt(orderResponse?.data)
        else
            orderResponse?.data?.let { orderDetails ->
                val orderSummary = orderDetails.order?.map { item ->
                    DummyModel.CartItem(
                        menuItem = DataItemMenu(
                            imageMenu = item?.menu?.image,
                            quota = item?.menu?.kuota,
                            noMenu = item?.menu?.nomorMenu,
                            nameMenu = item?.menu?.nama,
                            price = item?.menu?.discPrice ?: item?.menu?.harga,
                            idMenu = item?.menu?.id
                        ),
                        qty = item?.qty ?: 0,
                        selectedToppings = (item?.topping ?: emptyList()) as List<ToppingItem>,
                        note = item?.note ?: ""
                    )
                }?.let {
                    DummyModel.OrderSummary(
                        orderId = orderDetails.payment?.id,
                        listCartItems = it,
                        totalPurchase = orderDetails.payment?.totalPrice ?: 0,
                        totalDisc = 0
                    )
                }
                navigateFragment(
                    Destination.ORDER_TO_ORDER_MENU,
                    findNavController(),
                    Bundle().apply {
                        putString(
                            BUNDLE_ID_ORDER, orderDetails.payment?.id.toString()
                        )
                        putParcelable(
                            Key.BUNDLE_DATA_ORDER_TO_EDIT, orderSummary
                        )
                    })
            }
    }

    private fun printReceipt(orderResponse: DataItemOrder?) {
        Log.e("CHECK_NAMA", "nama resto : ${orderResponse?.locationName.toString()}")
        Log.e("CHECK_NAMA", "nama resto : $orderResponse")
        try {
            // Cetak pertama
            printReceiptContent(orderResponse)
            NotificationHandle.showSuccessSnackBar(requireView(), "Struk berhasil dicetak")
        } catch (e: Exception) {
            e.printStackTrace()
            NotificationHandle.showErrorSnackBar(
                requireView(), "Gagal mencetak struk: ${e.message}"
            )
        }
    }

    // Memisahkan logika cetak ke fungsi terpisah
    private fun printReceiptContent(orderResponse: DataItemOrder?) {
        val items = mutableListOf<Triple<String, Int, Int>>()

        orderResponse?.order?.forEach { item ->
            val menuName = item?.menu?.nama.toString()
            val note = item?.note
            val toppings = item?.topping?.joinToString(", ") { it?.nama ?: "" }

            // Hitung total harga item termasuk topping
            val basePrice = item?.menu?.harga ?: 0
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
            R.drawable.logo_sushi_key, DisplayMetrics.DENSITY_MEDIUM
        )?.toBitmap()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap!!, 300, 100, true)

        val logoHex = PrinterTextParserImg.bitmapToHexadecimalString(printer, resizedBitmap)

        var receiptText =
            "[C]<img>$logoHex</img>\n" + "[C]===============================\n" + "[L]No. Order: ${orderResponse?.payment?.noOrder}\n" + "[L]Nama: ${orderResponse?.payment?.buyerName}\n" + "[L]Nama Kasir: ${orderResponse?.staffName}\n" + "[L]Lokasi Resto: ${orderResponse?.locationName}\n" + "[C]===============================\n" + "[L]Qty Menu[R]Total\n" + "[C]-------------------------------\n"

        items.forEach { (nama, harga, qty) ->
            val totalHarga = harga * qty
            receiptText += "[L]$qty " + " $nama[R]${formatCurrency(totalHarga)}\n"
        }

        val specialPaymentMethods = setOf("QRIS", "TRANSFER", "GOJEK", "GRAB")

        val totalKeseluruhan = items.sumOf { it.second * it.third }

        val cash = if (orderResponse?.payment?.paymentMethod in specialPaymentMethods) {
            formatCurrency(orderResponse?.payment?.totalPrice ?: 0)
        } else {
            formatCurrency(orderResponse?.payment?.moneyPaid ?: 0)
        }

        val cashBack = if (orderResponse?.payment?.paymentMethod in specialPaymentMethods) {
            formatCurrency(0)
        } else {
            formatCurrency(
                orderResponse?.payment?.moneyPaid?.minus(
                    orderResponse.payment.totalPrice ?: 0
                ) ?: 0
            )
        }

        receiptText += "[C]-------------------------------\n" + "[L]Total[R] ${
            formatCurrency(
                totalKeseluruhan
            )
        }\n" + "[L]${orderResponse?.payment?.paymentMethod}[R] $cash\n" + "[L]Kembalian[R] $cashBack\n" + "[C]===============================\n" + "[L]Tanggal: ${Formatter.getCurrentDateAndTime()}\n" + "[L]Terima kasih!"

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