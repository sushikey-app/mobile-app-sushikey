package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.params.LocationBody
import com.am.projectinternalresto.data.response.super_admin.location.AddOrUpdateLocationResponse
import com.am.projectinternalresto.data.response.super_admin.location.DataItemLocation
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateLocationBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class AddOrUpdateLocationFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocationViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private val dataLocation: DataItemLocation? by lazy { arguments?.getParcelable(Key.BUNDLE_DATA_LOCATION) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateLocationBinding.inflate(inflater, container, false)
        setupDisplay()
        setupNavigation()
        return binding.root
    }

    private fun setupDisplay() {
        if (dataLocation != null) {
            binding.edtNameResto.setText(dataLocation?.outletName)
            binding.edtLocation.setText(dataLocation?.locationOutlet)
            binding.edtPhoneNumber.setText(dataLocation?.phoneNumber)
            binding.actionHeadline.textHeadline.text = buildString {
                append(R.string.edit_data)
                append(" ")
                append(R.string.location)
            }

        } else {
            binding.actionHeadline.textHeadline.text = getString(R.string.add_location)
        }
        binding.buttonAddLocation.text = getString(R.string.save_data)
        UiHandle.setupDisableHintForField(
            binding.edlNameResto,
            binding.edlLocation,
            binding.edlPhoneNumber
        )
    }

    private fun setupNavigation() {
        binding.actionHeadline.buttonBack.setOnClickListener { findNavController().popBackStack() }
        binding.buttonAddLocation.setOnClickListener {
            UiHandle.setupHideKeyboard(it)
            if (dataLocation != null) {
                setupPutDataLocationToApi()
            } else {
                setupPostDataLocationToApi()
            }
        }
    }

    private fun setupPostDataLocationToApi() {
        viewModel.addLocation(token, dataResultLocation())
            .observe(viewLifecycleOwner) { result ->
                handleStatusApi(result)
            }
    }

    private fun setupPutDataLocationToApi() {
        viewModel.updateLocation(token, dataLocation?.id.toString(), dataResultLocation())
            .observe(viewLifecycleOwner) { result -> handleStatusApi(result) }
    }

    private fun handleStatusApi(
        result: Resource<AddOrUpdateLocationResponse?>
    ) {
        when (result.status) {
            Status.LOADING -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar,
                    binding.textLoading,
                    false
                )
                NotificationHandle.showSuccessSnackBar(
                    requireView(),
                    result.data?.message.toString()
                )
                UiHandle.setupClearTextForField(
                    binding.edtNameResto,
                    binding.edtLocation,
                    binding.edtPhoneNumber
                )
                findNavController().popBackStack()
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false
                )
                NotificationHandle.showErrorSnackBar(
                    requireView(),
                    result.message.toString()
                )
            }
        }
    }

    private fun dataResultLocation(): LocationBody {
        return LocationBody(
            nameOutlet = binding.edtNameResto.text.toString(),
            locationOutlet = binding.edtLocation.text.toString(),
            phoneNumber = binding.edtPhoneNumber.text.toString()
        )
    }
}