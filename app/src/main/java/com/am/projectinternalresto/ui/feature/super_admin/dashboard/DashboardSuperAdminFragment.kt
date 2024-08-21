package com.am.projectinternalresto.ui.feature.super_admin.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentDashboardBinding
import com.am.projectinternalresto.ui.adapter.dahboard.MenuFavoriteAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.dialog_fragment.FilterSalesDialogFragment
import com.am.projectinternalresto.ui.widget.chart.CustomStyleRoundedBarChart
import com.am.projectinternalresto.utils.MyValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class DashboardSuperAdminFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val authViewModel : AuthViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupView()
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.textFilter.setOnClickListener {
            FilterSalesDialogFragment.show(childFragmentManager)
        }
    }

    private fun setupView() {
        setupCardMenuFavorite()
        setupTabLayout()
        setupChartSales()
        binding.cardIncomeDays.apply {
            textTitleContent.text = getString(R.string.text_income_today)
            textValueContent.text = "Rp. 200.000"
            iconContent.setImageResource(R.drawable.icon_income_today)
        }
        binding.cardIncomeMonth.apply {
            textTitleContent.text = getString(R.string.text_income_this_month)
            textValueContent.text = "Rp. 200.000"
            iconContent.setImageResource(R.drawable.icon_income_this_month)
        }
        binding.cardTotalSalesMonth.apply {
            textTitleContent.text = getString(R.string.text_total_order_today)
            textValueContent.text = "50"
            iconContent.setImageResource(R.drawable.icon_total_order)
        }
        binding.cardTotalSalesOfflineOrder.apply {
            textTitleContent.text = getString(R.string.text_order_offline)
            textValueContent.text = "30"
            iconContent.setImageResource(R.drawable.icon_order_offline)
        }
        binding.cardTotalSalesOnlineOrder.apply {
            textTitleContent.text = getString(R.string.text_order_online)
            textValueContent.text = "20"
            iconContent.setImageResource(R.drawable.icon_order_online)
        }
    }

    private fun setupChartSales() {
        val barChart = binding.cardChartSales.chartSales
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 5f))
        entries.add(BarEntry(1f, 6f))
        entries.add(BarEntry(2f, 7f))
        entries.add(BarEntry(3f, 8f))
        entries.add(BarEntry(4f, 6f))
        entries.add(BarEntry(5f, 5f))
        entries.add(BarEntry(6f, 7f))
        entries.add(BarEntry(7f, 6f))
        entries.add(BarEntry(8f, 5f))
        entries.add(BarEntry(9f, 7f))
        entries.add(BarEntry(10f, 6f))
        entries.add(BarEntry(11f, 5f))

        val dataSet = BarDataSet(entries, "Penjualan 2024")
        dataSet.color = Color.parseColor("#FF7F7F")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 10f

        val data = BarData(dataSet)
        barChart.data = data
        barChart.description.isEnabled = false

        // Set custom renderer
        barChart.renderer =
            CustomStyleRoundedBarChart(barChart, barChart.animator, barChart.viewPortHandler)

        // Customisasi tambahan
        barChart.setDrawGridBackground(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.axisRight.isEnabled = false

        // Set value formatter untuk sumbu X
        barChart.xAxis.valueFormatter = MyValueFormatter()
        barChart.xAxis.labelCount = 12
        barChart.xAxis.granularity = 1f

        barChart.animateY(1000)
        barChart.invalidate()
    }

    private fun setupCardMenuFavorite() {
        val adapter = MenuFavoriteAdapter()
        adapter.submitList(DummyData.dummyCardMenuFavorite)
        binding.cardMenuFavorite.apply {
            recyclerViewFavoriteMenu.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewFavoriteMenu.adapter = adapter
        }
    }


    private fun setupTabLayout() {
        val tabLayout = binding.cardMenuFavorite.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Makanan"))
        tabLayout.addTab(tabLayout.newTab().setText("Minuman"))

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Toast.makeText(requireContext(), tab.text.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}