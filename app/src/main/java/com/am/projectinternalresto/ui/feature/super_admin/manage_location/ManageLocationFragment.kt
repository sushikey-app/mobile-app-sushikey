package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.super_admin.location.LocationResponse
import com.am.projectinternalresto.databinding.FragmentManageLocationBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_location.ManageLocationAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import org.koin.android.ext.android.inject

class ManageLocationFragment : Fragment() {
    private var _binding: FragmentManageLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocationViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageLocationBinding.inflate(inflater, container, false)
        setupNavigation()
        setupGetDataFromAPI()
        return binding.root
    }

    private fun setupNavigation() {
        binding.cardManageLocation.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
                findNavController()
            )
        }
    }

    private fun setupGetDataFromAPI() {
        // function to get data location outlet
        viewModel.getLocation(token).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageLocation.shimmerLayout,
                        true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageLocation.shimmerLayout,
                        false
                    )
                    setupAdapter(resource.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageLocation.shimmerLayout,
                        false
                    )
                }
            }
        }
    }

    private fun setupPostDeleteData(idLocation: String) {
        viewModel.deleteLocation(token, idLocation).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {}

                Status.SUCCESS -> {
                    setupGetDataFromAPI()
                    NotificationHandle.showSuccessSnackBar(
                        requireView(),
                        resource.data?.message.toString()
                    )
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), resource.message.toString())
                    binding.cardManageLocation.recyclerViewContentTableLocation.visibility =
                        View.VISIBLE
                }
            }
        }
    }

    private fun setupAdapter(data: LocationResponse?) {
        val adapter = ManageLocationAdapter().apply {
            submitList(data?.data)
            callbackOnEditClickListener { dataLocation ->
                Navigation.navigateFragment(
                    Destination.MANAGE_LOCATION_TO_ADD_OR_UPDATE_LOCATION,
                    findNavController(),
                    Bundle().apply {
                        putParcelable(
                            Key.BUNDLE_DATA_LOCATION,
                            dataLocation
                        )
                    }
                )
            }
            callbackOnDeleteClickListener { idLocation ->
                setupPostDeleteData(idLocation)
            }
        }

        binding.cardManageLocation.recyclerViewContentTableLocation.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }
}