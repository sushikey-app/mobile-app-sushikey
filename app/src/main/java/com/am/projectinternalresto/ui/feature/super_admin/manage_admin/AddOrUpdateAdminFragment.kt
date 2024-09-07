package com.am.projectinternalresto.ui.feature.super_admin.manage_admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.body_params.AdminAndSuperAdminRequest
import com.am.projectinternalresto.data.response.super_admin.location.DataItemLocation
import com.am.projectinternalresto.data.response.super_admin.manage_admin.AddOrUpdateAdminSuperAdminResponse
import com.am.projectinternalresto.data.response.super_admin.manage_admin.DataItemManageAdminAndSuperAdmin
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateAdminBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_admin.SelectWorkLocationAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject
import com.am.projectinternalresto.ui.feature.auth.LoginActivity.Companion as KEY_LOGIN_ROLE

class AddOrUpdateAdminFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateAdminBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageAdminViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val locationViewModel: LocationViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private var selectedRole: String? = null
    private var selectIdLocation: String = ""
    private lateinit var adapterDropdownSelectAdminWork: SelectWorkLocationAdapter
    private val dataAdmin: DataItemManageAdminAndSuperAdmin? by lazy {
        arguments?.getParcelable(
            Key.BUNDLE_DATA_ADMIN_OR_SUPER_ADMIN
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateAdminBinding.inflate(inflater, container, false)
        setupDisplayDropdownRole()
        setupDisplayLayout()
        setupNavigation()
        setupGetDataLocationFromApi()
        return binding.root
    }

    private fun setupDisplayLayout() {
        UiHandle.setupDisableHintForField(
            binding.edlName,
            binding.edlUserName,
            binding.edlPassword,
            binding.edlPhoneNumber,
            binding.edlLocation,
            binding.edlRole
        )
        if (dataAdmin != null) {
            binding.actionHeadline.textHeadline.text = buildString {
                append(getString(R.string.edit_data))
                append(" ")
                append(dataAdmin?.role)

            }
            binding.edtUsername.setText(dataAdmin?.username)
            binding.edtName.setText(dataAdmin?.name)
            binding.edtPhoneNumber.setText(dataAdmin?.phoneNumber)
            binding.dropDownRole.setText(dataAdmin?.role)
            binding.dropdownLocation.setText(dataAdmin?.location?.locationOutlet.toString())
        } else {
            binding.actionHeadline.textHeadline.text = getString(R.string.add_admin)
        }
        binding.buttonSave.text = getString(R.string.save_data)
    }

    private fun setupGetDataLocationFromApi() {
        locationViewModel.getLocation(token).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setupDisplayDropdownLocation(resource.data?.data as List<DataItemLocation>)
                    adapterDropdownSelectAdminWork.notifyDataSetChanged()
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), resource.message.toString())
                }
            }
        }
    }

    private fun setupDisplayDropdownLocation(data: List<DataItemLocation>) {
        adapterDropdownSelectAdminWork =
            SelectWorkLocationAdapter(requireContext(), R.layout.item_content_dropdown, data)
        val view = binding.dropdownLocation
        view.setAdapter(adapterDropdownSelectAdminWork)
        view.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val selectWorkLocation = adapterDropdownSelectAdminWork.getItem(i)
            selectIdLocation = selectWorkLocation.id

            binding.dropdownLocation.setText(selectWorkLocation.outletName)
            setupGetDataLocationFromApi()
        }
    }

    private fun setupDisplayDropdownRole() {
        val role = listOf(
            KEY_LOGIN_ROLE.KEY_SUPER_ADMIN,
            KEY_LOGIN_ROLE.KEY_ADMIN,
        )
        val view = binding.dropDownRole
        val adapterDropdownRole = ArrayAdapter(
            requireContext(), R.layout.item_content_dropdown, role
        )
        view.setAdapter(adapterDropdownRole)
        view.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            selectedRole = adapterView.getItemAtPosition(i) as? String


            if (selectedRole.equals(KEY_LOGIN_ROLE.KEY_ADMIN)) {
                binding.textLocation.visibility = View.VISIBLE
                binding.edlLocation.visibility = View.VISIBLE
            } else {
                binding.textLocation.visibility = View.GONE
                binding.edlLocation.visibility = View.GONE
            }
        }
        if (selectedRole.equals(KEY_LOGIN_ROLE.KEY_ADMIN)) {
            binding.textLocation.visibility = View.VISIBLE
            binding.edlLocation.visibility = View.VISIBLE
        } else {
            binding.textLocation.visibility = View.GONE
            binding.edlLocation.visibility = View.GONE
        }
    }

    private fun setupNavigation() {
        binding.actionHeadline.buttonBack.setOnClickListener { findNavController().popBackStack() }
        binding.buttonSave.setOnClickListener {
            UiHandle.setupHideKeyboard(it)
            if (dataAdmin != null) {
                setupPutDataAdminToApi()
            } else {
                setupPostDataAdminToApi()
            }
        }
    }

    private fun setupPostDataAdminToApi() {
        Log.e("check", "data ${dataResultAdminOrSuperAdmin()}")
        viewModel.addAdminOrSuperAdmin(token, dataResultAdminOrSuperAdmin())
            .observe(viewLifecycleOwner) { result ->
                handleStatusApi(result)
            }
    }

    private fun setupPutDataAdminToApi() {
        viewModel.updateAdminOrSuperAdmin(
            token, dataAdmin?.id.toString(), dataResultAdminOrSuperAdmin()
        ).observe(viewLifecycleOwner) { result ->
            handleStatusApi(result)
        }
    }

    private fun handleStatusApi(result: Resource<AddOrUpdateAdminSuperAdminResponse?>) {
        when (result.status) {
            Status.LOADING -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, true,
                )
            }

            Status.SUCCESS -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false,
                )
                findNavController().popBackStack()
                NotificationHandle.showSuccessSnackBar(requireView(), result.data?.message.toString())
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false,
                )
                NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    private fun dataResultAdminOrSuperAdmin(): AdminAndSuperAdminRequest {
        return AdminAndSuperAdminRequest(
            locationId = selectIdLocation.toString() ?: dataAdmin?.locationId.toString(),
            name = binding.edtName.text.toString(),
            username = binding.edtUsername.text.toString(),
            phoneNumber = binding.edtPhoneNumber.text.toString(),
            password = binding.edtPassword.text.toString(),
            role = selectedRole ?: dataAdmin?.role.toString()
        )
    }
}