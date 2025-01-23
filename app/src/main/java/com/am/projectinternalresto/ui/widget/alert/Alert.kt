package com.am.projectinternalresto.ui.widget.alert

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.am.projectinternalresto.data.response.super_admin.location.Location
import com.am.projectinternalresto.databinding.CustomLayoutDialogPrintReportBinding
import com.am.projectinternalresto.databinding.CustomLayoutDialogToppingAndNoteBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.service.source.UserPreference
import com.am.projectinternalresto.ui.adapter.manage_location.SelectToppingAdapter
import com.am.projectinternalresto.ui.feature.auth.LoginActivity
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import com.am.projectinternalresto.utils.MyValueFormatter
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.UiHandle
import com.am.projectinternalresto.utils.goToActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

fun showLogoutAlert(context: Context) {
    val alertDialog = MaterialAlertDialogBuilder(context)
    alertDialog.apply {
        setTitle(R.string.title_nav_logout)
        setMessage("Apakah anda yakin keluar?")
        setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            if (context is Activity) {
                UserPreference.getInstance().clearToken()
                context.goToActivity(LoginActivity::class.java, withFinish = true)
            } else {
                UserPreference.getInstance().clearToken()
                val intent = Intent(context.applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            }
        }
        setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
    }
    alertDialog.create()
    alertDialog.show()
}

fun showConfirmDeleteReportAlert(context: Context, onYesListener: () -> Unit) {
    val alertDialog = MaterialAlertDialogBuilder(context)
    alertDialog.apply {
        setTitle(R.string.title_nav_logout)
        setMessage("Apakah anda yakin keluar?")
        setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            onYesListener.invoke()
        }
        setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
    }
    alertDialog.create()
    alertDialog.show()
}

fun showAlertAddToCart(
    context: Context, viewModel: ManageOrderMenuViewModel, menuItem: DataItemMenu
) {
    val builder = MaterialAlertDialogBuilder(context).create()
    val view =
        LayoutInflater.from(context).inflate(R.layout.custom_layout_dialog_topping_and_note, null)

    val buttonSave = view.findViewById(R.id.buttonSaveDialogAddToCart) as Button
    val recyclerView = view.findViewById(R.id.recyclerViewTopping) as RecyclerView
    val buttonClose = view.findViewById(R.id.buttonCloseDialog) as ImageView
    val edtNote = view.findViewById(R.id.edtNote) as TextInputEditText
    val txtIsEmpty = view.findViewById(R.id.textNoResultData) as TextView

    builder.setView(view)
    val toppingList = menuItem.topping?.map { topping ->
        ToppingItem(topping.nama, topping.id.toString(), topping.harga)
    }
    val adapter = SelectToppingAdapter().apply { submitList(toppingList) }
    if (toppingList.isNullOrEmpty()) txtIsEmpty.visibility = View.VISIBLE
    recyclerView.layoutManager = GridLayoutManager(context, 2)
    recyclerView.adapter = adapter

    buttonClose.setOnClickListener {
        builder.dismiss()
    }
    buttonSave.setOnClickListener {
        val selectedToppings = toppingList?.filter { it.isSelected }
        val note = edtNote.text.toString()

        viewModel.addToCartWithToppingsAndNote(menuItem, selectedToppings, note)
        builder.dismiss()
    }
    builder.setCanceledOnTouchOutside(false)
    builder.show()
}

//fun showAlertFilterAndPrintReportAdmin(
//    context: Context, callbackOnclickSave: (month: Int, year: Int) -> Unit
//) {
//    val binding = CustomLayoutDialogPrintReportBinding.inflate(LayoutInflater.from(context))
//    val builder = MaterialAlertDialogBuilder(context).create()
//    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
//    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
//
//    var selectedMonth: Int = currentMonth
//    var selectedYear: Int = currentYear
//
//    binding.textLocation.visibility = View.GONE
//    binding.edlLocationReport.visibility = View.GONE
//
//    UiHandle.setupDisableHintForField(binding.edlChooseYear, binding.edlChooseMonthReport)
//
//    binding.dropdownInitialDateReport.setOnClickListener {
//        val monthPicker = MonthPickerDialog(context)
//        monthPicker.setOnMonthSelectedListener { monthInt, monthName ->
//            binding.dropdownInitialDateReport.setText(monthName)
//            binding.dropdownInitialDateReport.tag = monthInt
//            selectedMonth = monthInt
//        }
//        monthPicker.show()
//    }
//
//    binding.dropdownChooseDeadlineDate.setOnClickListener {
//        val yearPicker = YearPickerDialog(context)
//        yearPicker.setOnYearSelectedListener { years ->
//            binding.dropdownChooseDeadlineDate.setText(years.toString())
//            selectedYear = years
//        }
//        yearPicker.show()
//    }
//
//    binding.buttonCloseDialog.setOnClickListener {
//        builder.dismiss()
//    }
//
//    binding.buttonSaveReport.setOnClickListener {
//        callbackOnclickSave.invoke(selectedMonth, selectedYear)
//        builder.dismiss()
//    }
//
//    builder.setCanceledOnTouchOutside(false)
//    builder.setView(binding.root)
//    builder.show()
//}


// cancel order
// yes
fun showAlertCancelOrder(context: Context, callbackOnYesCancelOrder: (() -> Unit)) {
    val alertDialog = MaterialAlertDialogBuilder(context)
    alertDialog.apply {
        setTitle("Batalkan Pesanan")
        setMessage("Apakah Anda yakin pesanan dibatalkan")
        setPositiveButton(context.getString(R.string.yes)) { dialog, _ ->
            callbackOnYesCancelOrder.invoke()
            dialog.dismiss()
        }
        setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
    }
    alertDialog.create()
    alertDialog.show()
}

// no
fun showAlertReasonCancelOrder(
    context: Context,
    callbackOnClickSave: ((String) -> Unit)
) {
    val binding = CustomLayoutDialogToppingAndNoteBinding.inflate(LayoutInflater.from(context))
    val builder = MaterialAlertDialogBuilder(context).create()


    binding.textTitleTopping.visibility = View.GONE
    binding.recyclerViewTopping.visibility = View.GONE

    binding.textHeadline.text = "Tidak Menerima Pembatalan"
    binding.textTitleNote.text = "Alasan"
    binding.edtNote.hint = "Berikan alasan anda!"
    binding.buttonSaveDialogAddToCart.text = "Kirim"

    binding.buttonCloseDialog.setOnClickListener {
        builder.dismiss()
    }

    binding.buttonSaveDialogAddToCart.setOnClickListener {
        callbackOnClickSave.invoke(binding.edtNote.text.toString())
        builder.dismiss()
    }

    builder.setCanceledOnTouchOutside(false)
    builder.setView(binding.root)
    builder.show()
}


fun showAlertSaveCustomerName(
    context: Context, callbackOnClickSave: ((String) -> Unit)
) {
    val binding = CustomLayoutDialogToppingAndNoteBinding.inflate(LayoutInflater.from(context))
    val builder = MaterialAlertDialogBuilder(context).create()

    binding.textTitleTopping.visibility = View.GONE
    binding.recyclerViewTopping.visibility = View.GONE

    binding.textHeadline.text = "Tambah Data Pembeli"
    binding.textTitleNote.text = "Masukan nama pembeli"

    binding.buttonCloseDialog.setOnClickListener {
        builder.dismiss()
    }

    binding.buttonSaveDialogAddToCart.setOnClickListener {
        callbackOnClickSave.invoke(binding.edtNote.text.toString())
        builder.dismiss()
    }

    builder.setCanceledOnTouchOutside(false)
    builder.setView(binding.root)
    builder.show()
}

fun showAlertFilterAdminAndStaff(
    context: Context,
    callbackOnclickSave: (startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int) -> Unit
) {
    // Initialize binding and dialog
    val binding = CustomLayoutDialogPrintReportBinding.inflate(LayoutInflater.from(context))
    val builder = MaterialAlertDialogBuilder(context).create()
    val formatter = MyValueFormatter()

    var selectedData = FilterData(
        locationId = "",
        startDate = 0,
        startMonth = 0,
        startYear = 0,
        endDate = 0,
        endMonth = 0,
        endYear = 0
    )

    binding.textLocation.visibility = View.GONE
    binding.edlLocationReport.visibility = View.GONE
    binding.dropdownLocationReport.visibility = View.GONE

    // Setup start date picker
    binding.dropdownInitialDateReport.setOnClickListener {
        showMonthDatePicker(context) { month, date, year ->
            selectedData = selectedData.copy(startMonth = month, startDate = date, startYear = year)
            if (selectedData.startDate != null) UiHandle.setupDisableHintForField(binding.edlChooseMonthReport)
            val monthName = formatter.getFormattedValue(month.toFloat())
            binding.dropdownInitialDateReport.setText(buildString {
                append(date)
                append(" ")
                append(monthName)
                append(" ")
                append(year)
            })
        }
    }

    // Setup end date picker
    binding.dropdownChooseDeadlineDate.setOnClickListener {
        showMonthDatePicker(context) { month, date, year ->
            selectedData = selectedData.copy(endMonth = month, endDate = date, endYear = year)
            if (selectedData.endDate != null) UiHandle.setupDisableHintForField(binding.edlChooseYear)
            val monthName = formatter.getFormattedValue(month.toFloat())
            binding.dropdownChooseDeadlineDate.setText(buildString {
                append(date)
                append(" ")
                append(monthName)
                append(" ")
                append(year)
            })
        }
    }

    // Setup buttons
    binding.buttonCloseDialog.setOnClickListener {
        builder.dismiss()
    }

    binding.buttonSaveReport.setOnClickListener {
        if (isValidDateRange(selectedData)) {
            callbackOnclickSave.invoke(
                selectedData.startDate,
                selectedData.startMonth + 1,
                selectedData.startYear,
                selectedData.endDate,
                selectedData.endMonth + 1,
                selectedData.endYear
            )
            builder.dismiss()
        } else {
            NotificationHandle.showErrorSnackBar(
                binding.root, "Periode akhir tidak boleh sebelum periode awal"
            )
        }
    }

    // Show dialog
    builder.apply {
        setCanceledOnTouchOutside(false)
        setView(binding.root)
        show()
    }
}

fun showAlertFilterAndPrintReportSuperAdmin(
    context: Context,
    viewModel: LocationViewModel,
    token: String,
    callbackOnclickSave: (locationId: String, startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int) -> Unit
) {
    // Initialize binding and dialog
    val binding = CustomLayoutDialogPrintReportBinding.inflate(LayoutInflater.from(context))
    val builder = MaterialAlertDialogBuilder(context).create()
    val formatter = MyValueFormatter()

    var selectedData = FilterData(
        locationId = "",
        startDate = 0,
        startMonth = 0,
        startYear = 0,
        endDate = 0,
        endMonth = 0,
        endYear = 0
    )

    // Setup UI elements
    setupLocationDropdown(context, viewModel, token, binding) { locationId ->
        selectedData = selectedData.copy(locationId = locationId)
    }

    // Setup start date picker
    binding.dropdownInitialDateReport.setOnClickListener {
        showMonthDatePicker(context) { month, date, year ->
            selectedData = selectedData.copy(startMonth = month, startDate = date, startYear = year)
            val monthName = formatter.getFormattedValue(month.toFloat())
            if (selectedData != null) UiHandle.setupDisableHintForField(binding.edlChooseMonthReport)
            binding.dropdownInitialDateReport.setText(buildString {
                append(date)
                append(" ")
                append(monthName)
                append(" ")
                append(year)
            })
        }
    }

    // Setup end date picker
    binding.dropdownChooseDeadlineDate.setOnClickListener {
        showMonthDatePicker(context) { month, date, year ->
            selectedData = selectedData.copy(endMonth = month, endDate = date, endYear = year)
            if (selectedData != null) UiHandle.setupDisableHintForField(binding.edlChooseYear)
            val monthName = formatter.getFormattedValue(month.toFloat())
            binding.dropdownChooseDeadlineDate.setText(buildString {
                append(date)
                append(" ")
                append(monthName)
                append(" ")
                append(year)
            })
        }
    }

    // Setup buttons
    binding.buttonCloseDialog.setOnClickListener {
        builder.dismiss()
    }

    binding.buttonSaveReport.setOnClickListener {
        Log.e("CheckSelectedDate", "data : $selectedData")
        if (isValidDateRange(selectedData)) {
            callbackOnclickSave.invoke(
                selectedData.locationId,
                selectedData.startDate,
                selectedData.startMonth + 1,
                selectedData.startYear,
                selectedData.endDate,
                selectedData.endMonth + 1,
                selectedData.endYear
            )
            builder.dismiss()
        } else {
            NotificationHandle.showErrorSnackBar(
                binding.root, "Periode akhir tidak boleh sebelum periode awal"
            )
        }
    }

    // Show dialog
    builder.apply {
        setCanceledOnTouchOutside(false)
        setView(binding.root)
        show()
    }
}

// Data class to hold selected values
private data class FilterData(
    val locationId: String, val startMonth: Int,
    val startDate: Int, val startYear: Int, val endMonth: Int,
    val endDate: Int, val endYear: Int
)

// Helper function to setup location dropdown
private fun setupLocationDropdown(
    context: Context,
    viewModel: LocationViewModel,
    token: String,
    binding: CustomLayoutDialogPrintReportBinding,
    onLocationSelected: (String) -> Unit
) {
    viewModel.getLocation(token).observe(context as LifecycleOwner) { resource ->
        when (resource.status) {
            Status.SUCCESS -> {
                val locations = resource.data?.data?.map {
                    Location(it?.id.toString(), it?.outletName.toString())
                } ?: emptyList()

                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    locations.map { it.name })
                binding.dropdownLocationReport.apply {
                    setAdapter(adapter)
                    setOnItemClickListener { _, _, position, _ ->
                        onLocationSelected(locations[position].id)
                    }
                }
            }

            Status.ERROR -> {
                NotificationHandle.showErrorSnackBar(binding.root, resource.message.toString())
            }

            Status.LOADING -> {}
        }
    }
}

// Helper function to show month and date picker
@SuppressLint("DiscouragedApi")
private fun showMonthDatePicker(
    context: Context, onDateSelected: (Int, Int, Int) -> Unit
) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(month, dayOfMonth, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        show()
    }
}

// Helper function to validate date range
private fun isValidDateRange(data: FilterData): Boolean {
    if (data.startYear == 0 && data.startMonth == 0 && data.startDate == 0 ||
        data.endYear == 0 && data.endMonth == 0 && data.endDate == 0
    ) {
        return false
    }

    val startCalendar = Calendar.getInstance().apply {
        set(data.startYear, data.startMonth, data.startDate)
    }
    val endCalendar = Calendar.getInstance().apply {
        set(data.endYear, data.endMonth, data.endDate)
    }

    return !endCalendar.before(startCalendar)
}
