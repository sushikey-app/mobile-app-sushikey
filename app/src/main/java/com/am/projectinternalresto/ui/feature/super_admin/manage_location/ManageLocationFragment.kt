package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.databinding.FragmentManageLocationBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_location.ManageLocationAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertDeleteData
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class ManageLocationFragment : Fragment() {
    private var _binding: FragmentManageLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocationViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private lateinit var adapter: ManageLocationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageLocationBinding.inflate(inflater, container, false)
        setupManageLocationAdapter()
        setupView()
        setupNavigation()
        return binding.root
    }

    private fun setupView() {
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataFromApi() }
        UiHandle.setupClearTextForField(binding.search.edtSearch)
        setupGetDataFromApi()
        UiHandle.setupDisplayDataFromSearchOrGet(
            editLayout = binding.search.edlSearch,
            editText = binding.search.edtSearch,
            onSearchDisplayData = { keyword -> setupSearchLocationByLocationOutlet(keyword) },
            onDisplayDataDefault = { setupGetDataFromApi() }
        )
    }

    private fun setupNavigation() {
        binding.cardManageLocation.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
                findNavController()
            )
        }
    }

    private fun setupSearchLocationByLocationOutlet(locationOutlet: String) {
        viewModel.searchLocation(token, locationOutlet).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupGetDataFromApi() {
        // function to get data location outlet
        viewModel.getLocation(token).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(result.data?.data)
            }
        }
    }

    private fun setupPostDeleteData(idLocation: String) {
        viewModel.deleteLocation(token, idLocation).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result, result.message.toString()) {
                adapter.submitList(null)
                setupGetDataFromApi()
            }
        }
    }

    private fun setupManageLocationAdapter() {
        adapter = ManageLocationAdapter().apply {
            callbackOnEditClickListener { dataLocation ->

                Navigation.navigateFragment(
                    Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_LOCATION, dataLocation) }
                )
            }
            callbackOnDeleteClickListener { idLocation ->
                showAlertDeleteData(requireContext(), "Lokasi", "Lokasi") {
                    setupPostDeleteData(idLocation)
                }
            }
        }

        binding.cardManageLocation.recyclerViewContentTableLocation.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
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
                    binding.cardManageLocation.shimmerLayout,
                    true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageLocation.shimmerLayout,
                    false
                )
                onSuccess.invoke()
                binding.swipeRefreshLayout.isRefreshing = false
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                    binding.cardManageLocation.shimmerLayout,
                    false
                )
                binding.swipeRefreshLayout.isRefreshing = false
                NotificationHandle.showErrorSnackBar(requireView(), errorMessage)
            }
        }
    }
}