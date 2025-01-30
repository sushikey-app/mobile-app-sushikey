package com.am.projectinternalresto.ui.feature.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.data.response.super_admin.dashboard.DataItemSales
import com.am.projectinternalresto.data.response.super_admin.dashboard.MenuFavoriteResponse
import com.am.projectinternalresto.databinding.FragmentDashboardBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.dahboard.MenuFavoriteSuperAdminAdapter
import com.am.projectinternalresto.ui.feature.admin.manage_category.ManageCategoryViewModel
import com.am.projectinternalresto.ui.feature.admin.manage_menu.ManageMenuViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.widget.chart.Chart
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import java.util.Calendar

class DashboardAdminFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by inject()
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val categoryViewModel: ManageCategoryViewModel by inject()
    private val menuViewModel: ManageMenuViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupGetDataCategory()
        setupGetDataSales()
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupGetDataSales()
            setupGetDataCategory()
        }
    }

    private fun setupGetDataCategory() {
        categoryViewModel.getCategoryMenu(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(
                result,
                result.message.toString()
            ) {
                setupTabLayout(result.data?.data as List<DataItemCategory>)
            }
        }
    }

    private fun setupGetMenuFavorite(categoryId: String) {
        menuViewModel.getMenuFavoriteAdmin(token, categoryId)
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result, result.message.toString()) {
                    setupMenuFavoriteAdapter(result.data)
                }
            }
    }

    private fun setupGetDataSales() {
        viewModel.getDataSalesAdmin(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                setupShimmerVisibility(false)
                setupDashboardViewWithData(result.data?.data)
                Chart.setupSalesChart(
                    binding.cardChartSales.chartSales,
                    result.data?.data?.yearlyRevenue
                )
            }
        }
    }

    private fun setupDashboardViewWithData(data: DataItemSales?) {
        binding.cardChartSales.textYear.text = Calendar.getInstance().get(Calendar.YEAR).toString()
        binding.cardChartSales.textTitleCard.text = getString(R.string.sales)
        binding.cardIncomeDays.apply {
            textTitleContent.text = getString(R.string.text_income_today)
            textValueContent.text = Formatter.formatCurrency(data?.todayRevenue ?: 0)
            iconContent.setImageResource(R.drawable.icon_income_today)
        }
        binding.cardIncomeMonth.apply {
            textTitleContent.text = getString(R.string.text_income_this_month)
            textValueContent.text = Formatter.formatCurrency(data?.currentMonthRevenue ?: 0)
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


    private fun setupMenuFavoriteAdapter(data: MenuFavoriteResponse?) {
        val adapter = MenuFavoriteSuperAdminAdapter().apply { submitList(data?.data) }
        binding.cardMenuFavorite.apply {
            recyclerViewFavoriteMenu.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewFavoriteMenu.adapter = adapter
        }
    }

    private fun setupTabLayout(data: List<DataItemCategory>) {
        val tabLayout = binding.cardMenuFavorite.tabLayout
        tabLayout.removeAllTabs()

        data.distinctBy { it.id }.forEach { location ->
            val tab = tabLayout.newTab().apply {
                text = location.nameCategory
                tag = location.id
            }
            tabLayout.addTab(tab)
        }

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(8, 0, 8, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val categoryId = tab.tag as String
                setupGetMenuFavorite(categoryId)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        if (tabLayout.tabCount > 0) {
            val firstTab = tabLayout.getTabAt(0)
            firstTab?.let {
                it.select() // Pilih tab pertama
                val categoryId = it.tag as String
                setupGetMenuFavorite(categoryId)
            }
        }
    }


    private fun setupShimmerVisibility(isVisible: Boolean) {
        val cards = listOf(
            binding.cardIncomeMonth,
            binding.cardIncomeDays,
            binding.cardTotalSalesMonth,
            binding.cardTotalSalesOfflineOrder,
            binding.cardTotalSalesOnlineOrder
        )
        binding.cardChartSales.apply {
            ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutFirst, isVisible)
            ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutSecond, isVisible)
            ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutThird, isVisible)
        }
        cards.forEach { card ->
            card.apply {
                ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutFirst, isVisible)
                ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutSecond, isVisible)
                ProgressHandle.setupVisibilityShimmerLoadingLayout(shimmerLayoutThird, isVisible)
            }
        }
    }

    private fun <T> handleApiStatus(
        result: Resource<T>,
        errorMessage: String,
        onSuccess: () -> Unit
    ) {
        when (result.status) {
            Status.LOADING -> {
                setupShimmerVisibility(true)
            }

            Status.SUCCESS -> {
                setupShimmerVisibility(false)
                binding.swipeRefreshLayout.isRefreshing = false
                onSuccess.invoke()
            }

            Status.ERROR -> {
                setupShimmerVisibility(false)
                binding.swipeRefreshLayout.isRefreshing = false
                NotificationHandle.showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }

}