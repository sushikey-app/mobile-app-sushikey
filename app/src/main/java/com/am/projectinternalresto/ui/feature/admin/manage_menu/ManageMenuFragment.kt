package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.databinding.FragmentManageMenuBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_menu.ManageMenuAdapter
import com.am.projectinternalresto.ui.feature.admin.manage_category.ManageCategoryViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertDeleteData
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class ManageMenuFragment : Fragment() {
    private var _binding: FragmentManageMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val categoryViewModel: ManageCategoryViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var adapter: ManageMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageMenuBinding.inflate(inflater, container, false)
        setupView()
        setupManageMenuAdapter()
        setupNavigation()
        return binding.root
    }

    private fun setupView() {
        setupGetDataFromApi()
        setupGetDataCategory()
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupGetDataFromApi()
            setupGetDataCategory()
        }
        UiHandle.setupDisplayDataFromSearchOrGet(
            editLayout = binding.search.edlSearch,
            editText = binding.search.edtSearch,
            onSearchDisplayData = { keyword -> setupSearchMenu(keyword) },
            onDisplayDataDefault = { setupGetDataFromApi() }
        )
    }


    private fun setupGetDataFromApi() {
        viewModel.getMenu(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupGetDataCategory() {
        categoryViewModel.getCategoryMenu(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setupTabLayoutCategory(result.data?.data as List<DataItemCategory>)
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }


    private fun setupTabLayoutCategory(categories: List<DataItemCategory>) {
        val tabLayout = binding.cardManageMenu.tabLayout

        tabLayout.addTab(tabLayout.newTab().setText("All"))

        for (category in categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category.nameCategory))
        }

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(8, 0, 8, 0)
            tab.requestLayout()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                if (position == 0) {
                    adapter.submitList(emptyList())
                    setupGetDataFromApi()
                } else {
                    val selectedCategory = categories[position - 1]
                    adapter.submitList(emptyList())
                    setupFilterMenuByCategory(selectedCategory.id)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


    private fun setupFilterMenuByCategory(idMenu: String) {
        viewModel.filterMenuByCategory(token, idMenu).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    result.data?.data?.let { adapter.submitList(it) }
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }


    private fun setupSearchMenu(keyword: String) {
        viewModel.searchMenu(token, keyword).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupDeleteDataMenu(idMenu: String) {
        viewModel.deleteMenu(token, idMenu).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(null)
                setupGetDataFromApi()
            }
        }
    }

    private fun setupManageMenuAdapter() {
        adapter = ManageMenuAdapter().apply {
            callbackOnEditClickListener { menu ->
                Navigation.navigateFragment(
                    Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_MENU, menu) }
                )
            }
            callbackOnDeleteClickListener { idMenu ->
                showAlertDeleteData(requireContext(), "Menu", "menu") {
                    setupDeleteDataMenu(idMenu)
                }
            }
        }

        binding.cardManageMenu.recyclerViewContentTableLocation.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupNavigation() {
        binding.cardManageMenu.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_MENU_TO_ADD_OR_UPDATE_MENU,
                findNavController()
            )
        }
    }

    private fun <T> handleApiStatus(
        result: Resource<T>,
        errorMessage: String,
        onSuccess: () -> Unit
    ) {
        when (result.status) {
            Status.LOADING -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageMenu.shimmerLayout,
                    true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageMenu.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                onSuccess.invoke()
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageMenu.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                NotificationHandle.showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }
}