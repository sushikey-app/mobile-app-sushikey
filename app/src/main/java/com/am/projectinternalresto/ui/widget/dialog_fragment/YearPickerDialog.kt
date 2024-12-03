package com.am.projectinternalresto.ui.widget.dialog_fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.DialogMonthPickerBinding
import java.util.Calendar

class YearPickerDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogMonthPickerBinding
    private var selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var onYearSelectedListener: ((Int) -> Unit)? = null
    private val yearRange = (2020..Calendar.getInstance().get(Calendar.YEAR) + 12).toList()
    private val yearButtons = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMonthPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window?.apply {
            val width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        setupYearGrid()
        setupButtons()
        updateSelectedYear(selectedYear)
    }

    private fun setupYearGrid() {
        val gridLayout = binding.gridlayout
        val columnCount = 4
        val rowCount = (yearRange.size + columnCount - 1) / columnCount
        gridLayout.rowCount = rowCount

        yearRange.forEachIndexed { index, year ->
            val yearView = LayoutInflater.from(context)
                .inflate(R.layout.item_month, gridLayout, false) as TextView

            yearView.text = year.toString()
            yearView.setOnClickListener {
                updateSelectedYear(year)
            }

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(index % columnCount, 1f)
                rowSpec = GridLayout.spec(index / columnCount)
                setMargins(12, 12, 12, 12)
            }

            yearView.layoutParams = params
            gridLayout.addView(yearView)
            yearButtons.add(yearView)
        }
    }

    private fun updateSelectedYear(year: Int) {
        selectedYear = year
        binding.tvSelectedMonth.text = year.toString()

        yearButtons.forEach { button ->
            if (button.text.toString().toInt() == year) {
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
            Log.e("Check", "check year $selectedYear")
            onYearSelectedListener?.invoke(selectedYear)
            dismiss()
        }
    }

    fun setOnYearSelectedListener(listener: (Int) -> Unit) {
        onYearSelectedListener = listener
    }
}
