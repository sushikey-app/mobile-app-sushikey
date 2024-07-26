package com.am.projectinternalresto.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

object Formatter {
    fun formatCurrency(amount: Int): String {
        val decimal = DecimalFormat("#,###.##")
        return "Rp. ${decimal.format(amount)}"
    }
}

class MyValueFormatter : ValueFormatter() {
    private val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Juni", "Juli", "Agt", "Sep", "Okt", "Nov", "Des"
    )

    override fun getFormattedValue(value: Float): String {
        return months.getOrNull(value.toInt()) ?: value.toString()
    }
}
