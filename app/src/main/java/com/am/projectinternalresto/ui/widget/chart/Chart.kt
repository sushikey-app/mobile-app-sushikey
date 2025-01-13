package com.am.projectinternalresto.ui.widget.chart

import android.graphics.Color
import com.am.projectinternalresto.data.response.super_admin.dashboard.YearlyRevenue
import com.am.projectinternalresto.utils.MyValueFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

object Chart {
    fun setupSalesChart(barChart: BarChart, yearlyRevenue: YearlyRevenue?) {
        val entries = ArrayList<BarEntry>().apply {
            add(BarEntry(0f, yearlyRevenue?.january?.toFloat() ?: 0.0f))
            add(BarEntry(1f, yearlyRevenue?.february?.toFloat() ?: 0.0f))
            add(BarEntry(2f, yearlyRevenue?.march?.toFloat() ?: 0.0f))
            add(BarEntry(3f, yearlyRevenue?.april?.toFloat() ?: 0.0f))
            add(BarEntry(4f, yearlyRevenue?.may?.toFloat() ?: 0.0f))
            add(BarEntry(5f, yearlyRevenue?.june?.toFloat() ?: 0.0f))
            add(BarEntry(6f, yearlyRevenue?.july?.toFloat() ?: 0.0f))
            add(BarEntry(7f, yearlyRevenue?.august?.toFloat() ?: 0.0f))
            add(BarEntry(8f, yearlyRevenue?.september?.toFloat() ?: 0.0f))
            add(BarEntry(9f, yearlyRevenue?.october?.toFloat() ?: 0.0f))
            add(BarEntry(10f, yearlyRevenue?.november?.toFloat() ?: 0.0f))
            add(BarEntry(11f, yearlyRevenue?.december?.toFloat() ?: 0.0f))
        }

        val dataSet = BarDataSet(entries, "Penjualan 2024").apply {
            color = Color.parseColor("#FF7F7F")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        val barData = BarData(dataSet)
        barChart.apply {
            data = barData
            description.isEnabled = false
            setDrawGridBackground(false)
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
            xAxis.valueFormatter = MyValueFormatter()
            xAxis.labelCount = 12
            xAxis.granularity = 1f
            animateY(1000)
            invalidate()
        }
    }
}