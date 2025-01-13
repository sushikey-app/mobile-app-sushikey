package com.am.projectinternalresto.ui.widget.dialog_fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.DialogMonthPickerBinding
import java.util.Calendar

class MonthPickerDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogMonthPickerBinding
    private var selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var onMonthSelectedListener: ((Int, String) -> Unit)? = null
    private val months = arrayOf(
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    )
    private val monthButtons = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMonthPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.apply {
            val width = (context.resources.displayMetrics.widthPixels * 0.5).toInt()
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setupMonthGrid()
        setupButtons()
        updateSelectedMonth(selectedMonth)
    }

    private fun setupMonthGrid() {
        val gridLayout = binding.gridlayout

        months.forEachIndexed { index, month ->
            val monthView = LayoutInflater.from(context)
                .inflate(R.layout.item_month, gridLayout, false) as TextView

            monthView.text = month
            monthView.setOnClickListener {
                updateSelectedMonth(index)
            }

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(index % 4, 1f)
                rowSpec = GridLayout.spec(index / 4)
                setMargins(12, 12, 12, 12)
            }

            monthView.layoutParams = params
            gridLayout.addView(monthView)
            monthButtons.add(monthView)
        }
    }

    private fun updateSelectedMonth(index: Int) {
        selectedMonth = index
        binding.tvSelectedMonth.text = months[index]

        monthButtons.forEachIndexed { buttonIndex, button ->
            if (buttonIndex == index) {
                button.setBackgroundResource(R.drawable.bg_month_selected)
                button.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            } else {
                button.setBackgroundResource(R.drawable.bg_month_unselected)
                button.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            }
        }
    }

    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            val monthName = months[selectedMonth]
            val monthInt = selectedMonth + 1
            onMonthSelectedListener?.invoke(monthInt, monthName)
            dismiss()
        }
    }

    fun setOnMonthSelectedListener(listener: (Int, String) -> Unit) {
        onMonthSelectedListener = listener
    }
}

