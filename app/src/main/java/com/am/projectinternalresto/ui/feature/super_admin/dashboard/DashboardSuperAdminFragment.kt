package com.am.projectinternalresto.ui.feature.super_admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.super_admin.dashboard.DataItemSales
import com.am.projectinternalresto.data.response.super_admin.dashboard.MenuFavoriteResponse
import com.am.projectinternalresto.data.response.super_admin.location.DataItemLocation
import com.am.projectinternalresto.databinding.FragmentDashboardBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.dahboard.MenuFavoriteSuperAdminAdapter
import com.am.projectinternalresto.ui.feature.admin.manage_menu.ManageMenuViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import com.am.projectinternalresto.ui.widget.chart.Chart.setupSalesChart
import com.am.projectinternalresto.utils.Formatter.formatCurrency
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import java.util.Calendar

class DashboardSuperAdminFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by inject()
    private val viewModel: ManageOrderMenuViewModel by inject()
    private val locationViewModel: LocationViewModel by inject()
    private val menuViewModel: ManageMenuViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupGetDataSales()
        setupGetAllLocation()
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
//        binding.textFilter.setOnClickListener {
//            FilterSalesDialogFragment.show(childFragmentManager)
//        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupGetDataSales()
            setupGetAllLocation()
        }
    }


    private fun setupGetAllLocation() {
        locationViewModel.getLocation(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result = result, result.message.toString()) {
                setupTabLayout(result.data?.data as List<DataItemLocation>)
            }
        }
    }

    private fun setupGetMenuFavorite(locationId: String) {
        menuViewModel.getMenuFavoriteSuperAdmin(token, locationId)
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        setupMenuFavoriteAdapter(result.data)
                    }

                    Status.ERROR -> {
                        NotificationHandle.showErrorSnackBar(
                            requireView(),
                            result.message.toString()
                        )
                    }
                }
            }
    }

    private fun setupGetDataSales() {
        viewModel.getDataSales(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                setupDashboardViewWithData(result.data?.data)
                setupSalesChart(
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


    private fun setupMenuFavoriteAdapter(data: MenuFavoriteResponse?) {
        val adapter = MenuFavoriteSuperAdminAdapter().apply { submitList(data?.data) }
        binding.cardMenuFavorite.apply {
            recyclerViewFavoriteMenu.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewFavoriteMenu.adapter = adapter
        }
    }

    private fun setupTabLayout(data: List<DataItemLocation>) {
        val tabLayout = binding.cardMenuFavorite.tabLayout
        tabLayout.removeAllTabs()

        data.distinctBy { it.id }.forEach { location ->
            val tab = tabLayout.newTab().apply {
                text = location.outletName
                tag = location.id
            }
            tabLayout.addTab(tab)
        }

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val locationId = tab.tag as String
                setupGetMenuFavorite(locationId)
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