package com.am.projectinternalresto.ui.feature.admin.manage_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.FragmentManageCategoryBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_category.ManageCategoryAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertDeleteData
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class ManageCategoryFragment : Fragment() {
    private var _binding: FragmentManageCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageCategoryViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var adapter: ManageCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCategoryBinding.inflate(inflater, container, false)
        setupManageCategoryAdapter()
        setupView()
        setupNavigation()
        return binding.root
    }

    private fun setupView() {
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataFromApi() }
        setupGetDataFromApi()
        UiHandle.setupDisplayDataFromSearchOrGet(
            editLayout = binding.search.edlSearch,
            editText = binding.search.edtSearch,
            onSearchDisplayData = { keyword -> setupSearchCategoryByName(keyword) },
            onDisplayDataDefault = { setupGetDataFromApi() }
        )
    }

    private fun setupNavigation() {
        /*add new category*/
        binding.cardManageCategory.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY, findNavController()
            )
        }

        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataFromApi() }
    }

    private fun setupGetDataFromApi() {
        viewModel.getCategoryMenu(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
                if (result.data == null) {
                    binding.cardManageCategory.textDataIsNull.apply {
                        text = getString(R.string.data_category_is_empty)
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupSearchCategoryByName(keyword: String) {
        viewModel.searchCategoryMenu(token, keyword).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
                if (result.data == null) {
                    binding.cardManageCategory.textDataIsNull.apply {
                        text = getString(R.string.data_category_is_null_or_empty)
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupDeleteCategoryMenu(idCategory: String) {
        viewModel.deleteCategoryMenu(token, idCategory).observe(viewLifecycleOwner) { result ->
            handleApiStatus(
                result = result,
                result.message.toString()
            ) {
                adapter.submitList(null)
                setupGetDataFromApi()
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
                adapter.submitList(emptyList())
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageCategory.shimmerLayout,
                    true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageCategory.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                onSuccess.invoke()
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageCategory.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                NotificationHandle.showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }

    private fun setupManageCategoryAdapter() {
        adapter = ManageCategoryAdapter().apply {
            callbackOnEditClickListener { dataCategory ->
                Navigation.navigateFragment(
                    Destination.MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_CATEGORY, dataCategory) }
                )
            }
            callbackOnDeleteClickListener { idCategory ->
                showAlertDeleteData(
                    requireContext(),
                    "Kategori",
                    "kategori"
                ) {
                    setupDeleteCategoryMenu(idCategory)
                }
            }
        }
        binding.cardManageCategory.recyclerViewCategory.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
