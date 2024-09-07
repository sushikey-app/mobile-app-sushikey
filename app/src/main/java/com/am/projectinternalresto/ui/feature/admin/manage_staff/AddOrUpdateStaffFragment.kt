package com.am.projectinternalresto.ui.feature.admin.manage_staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.body_params.StaffRequest
import com.am.projectinternalresto.data.response.admin.manage_staff.AddOrUpdateStaffResponse
import com.am.projectinternalresto.data.response.admin.manage_staff.DataItemStaff
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateStaffBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class AddOrUpdateStaffFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateStaffBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageStaffViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val user: DataItemStaff? by lazy { arguments?.getParcelable(Key.BUNDLE_DATA_STAFF) }
    private val token: String by lazy { authViewModel.getTokenUser().toString() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateStaffBinding.inflate(inflater, container, false)
        setupDisplayWidget()
        setupNavigation()
        return binding.root
    }

    private fun setupDisplayWidget() {
        UiHandle.setupDisableHintForField(
            binding.edlName,
            binding.edlName,
            binding.edlUsername,
            binding.edlNumberTelephone,
            binding.edlPassword
        )
        if (user != null) {
            binding.actionHeadline.textHeadline.text = buildString {
                append(getString(R.string.edit_data))
                append(" ")
                append("Pegawai")
            }
            binding.edtName.setText(user?.name)
            binding.edtUsername.setText(user?.username)
            binding.edtNumberTelephone.setText(user?.phoneNumber)
        } else {
            binding.actionHeadline.textHeadline.text = buildString {
                append(R.string.add_data)
                append(" ")
                append("Pegawai")
            }
        }
        binding.buttonAddOrUpdateStaff.text = getString(R.string.save_data)
    }

    private fun setupNavigation() {
        binding.actionHeadline.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonAddOrUpdateStaff.setOnClickListener {
            UiHandle.setupHideKeyboard(it)
            if (user != null) {
                setupPutDataStaffToApi()
            } else {
                setupPostDataStaffToApi()
            }
        }
    }

    private fun setupPostDataStaffToApi() {
        viewModel.addStaff(token, dataStaff()).observe(viewLifecycleOwner) { result ->
            handleApiStatus(result)
        }
    }

    private fun setupPutDataStaffToApi() {
        viewModel.updateStaff(token, user?.id.toString(), dataStaff())
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result)
            }
    }

    private fun handleApiStatus(result: Resource<AddOrUpdateStaffResponse?>) {
        when (result.status) {
            Status.LOADING -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, true
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false
                )
                NotificationHandle.showSuccessSnackBar(
                    requireView(), result.data?.message.toString()
                )
                findNavController().popBackStack()
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false
                )
                NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    private fun dataStaff(): StaffRequest {
        return StaffRequest(
            name = binding.edtName.text.toString(),
            username = binding.edtUsername.text.toString(),
            phoneNumber = binding.edtNumberTelephone.text.toString(),
            password = binding.edtPassword.text.toString()
        )
    }
}