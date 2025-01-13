package com.am.projectinternalresto.ui.widget.dialog_fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.am.projectinternalresto.data.response.staff.order.PayResponse
import com.am.projectinternalresto.databinding.CustomLayoutDialogPrintStrukBinding
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.am.projectinternalresto.utils.Formatter.getCurrentDate
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PaymentSuccessDialog(
    private val dataPayOrder: PayResponse?,
    private val onDismiss: (Boolean) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = CustomLayoutDialogPrintStrukBinding.inflate(layoutInflater)
        val priceReturn = dataPayOrder?.data?.payment?.uangDibayarkan?.minus(
            dataPayOrder.data.payment.totalHarga ?: 0
        )
        binding.textValuePaymentMethod.text = dataPayOrder?.data?.payment?.metode.toString()
        binding.textValuePay.text =
            formatCurrency(dataPayOrder?.data?.payment?.uangDibayarkan ?: 0)
        if (dataPayOrder?.data?.payment?.metode == "Qris") {
            binding.textValuePay.text = formatCurrency(0)
            binding.textValuePriceReturn.text = formatCurrency(0)
        } else {
            binding.textValuePay.text =
                formatCurrency(dataPayOrder?.data?.payment?.totalHarga ?: 0)
            binding.textValuePriceReturn.text = formatCurrency(priceReturn ?: 0)
        }
        binding.textValueDateTransaction.text = getCurrentDate()
        binding.buttonDone.setOnClickListener {
            onDismiss(false)
            dismiss()
        }

        binding.buttonPrint.setOnClickListener {
            onDismiss(true)
        }

        return MaterialAlertDialogBuilder(requireContext()).setView(binding.root).create()
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            payResponse: PayResponse?,
            onDismiss: (Boolean) -> Unit
        ) {
            val paymentSuccessDialog = PaymentSuccessDialog(payResponse, onDismiss)
            paymentSuccessDialog.show(fragmentManager, paymentSuccessDialog.tag)
        }
    }
}