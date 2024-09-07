package com.am.projectinternalresto.ui.feature.super_admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.data.response.super_admin.dashboard.DataItemSales
import com.am.projectinternalresto.databinding.FragmentDashboardBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.dahboard.MenuFavoriteAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.widget.chart.Chart.setupSalesChart
import com.am.projectinternalresto.ui.widget.dialog_fragment.FilterSalesDialogFragment
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class DashboardSuperAdminFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by inject()
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupCardMenuFavorite()
        setupGetData()
        setupTabLayout()
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.textFilter.setOnClickListener {
            FilterSalesDialogFragment.show(childFragmentManager)
        }
    }

    private fun setupGetData() {
        viewModel.getSalesData(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    setupShimmerVisibility(true)
                }

                Status.SUCCESS -> {
                    setupShimmerVisibility(false)
                    setupDashboardViewWithData(result.data?.data)
                    setupSalesChart(
                        binding.cardChartSales.chartSales,
                        result.data?.data?.yearlyRevenue
                    )
                }

                Status.ERROR -> {
                    setupShimmerVisibility(false)
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupDashboardViewWithData(data: DataItemSales?) {
        binding.cardIncomeDays.apply {
            textTitleContent.text = getString(R.string.text_income_today)
            textValueContent.text = formatCurrency(data?.todayRevenue ?: 0)
            iconContent.setImageResource(R.drawable.icon_income_today)
        }
        binding.cardIncomeMonth.apply {
            textTitleContent.text = getString(R.string.text_income_this_month)
            textValueContent.text = formatCurrency(data?.currentMonthRevenue ?: 0)
            iconContent.setImageResource(R.drawable.icon_income_this_month)
        }
        binding.cardTotalSalesMonth.apply {
            textTitleContent.text = getString(R.string.text_total_order_today)
            textValueContent.text = data?.todayTotalSales.toString()
            iconContent.setImageResource(R.drawable.icon_total_order)
        }
        binding.cardTotalSalesOfflineOrder.apply {
            textTitleContent.text = getString(R.string.text_order_offline)
            textValueContent.text = data?.offlineOrders.toString()
            iconContent.setImageResource(R.drawable.icon_order_offline)
        }
        binding.cardTotalSalesOnlineOrder.apply {
            textTitleContent.text = getString(R.string.text_order_online)
            textValueContent.text = data?.onlineOrders.toString()
            iconContent.setImageResource(R.drawable.icon_order_online)
        }
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

    private fun setupShimmerVisibility(isVisible: Boolean) {
        val cards = listOf(
            binding.cardIncomeMonth,
            binding.cardIncomeDays,
            binding.cardTotalSalesMonth,
            binding.cardTotalSalesOfflineOrder,
            binding.cardTotalSalesOnlineOrder
        )

        cards.forEach { card ->
            card.apply {
                ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutFirst, isVisible)
                ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutSecond, isVisible)
                ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerThird, isVisible)
            }
        }
    }

}