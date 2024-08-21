package com.am.projectinternalresto.ui.feature.super_admin.manage_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.super_admin.manage_admin.ManageAdminAndSuperAdminResponse
import com.am.projectinternalresto.databinding.FragmentManageEmployeesBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_admin.ManageAdminAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import org.koin.android.ext.android.inject

class ManageAdminFragment : Fragment() {
    private var _binding: FragmentManageEmployeesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageAdminViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageEmployeesBinding.inflate(inflater, container, false)
        setupGetDataFromApi()
        setupNavigation()
        return binding.root
    }

    private fun setupGetDataFromApi() {
        viewModel.getAllDataAdminAndSuperAdmin(token).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageAdmin.shimmerLayout, true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageAdmin.shimmerLayout, false
                    )
                    setupAdapterManageAdminAndSuperAdmin(resource.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoading(
                        binding.cardManageAdmin.shimmerLayout, false
                    )
                    NotificationHandle.showErrorSnackBar(requireView(), resource.message.toString())
                }
            }
        }
    }

    private fun setupDeleteDataAdminOrSuperAdmin(id: String) {
        viewModel.deleteAdminOrSuperAdmin(token, id).observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.LOADING -> {}

                    Status.SUCCESS -> {
                        setupGetDataFromApi()
                        NotificationHandle.showSuccessSnackBar(
                            requireView(), resource.data?.message.toString()
                        )
                    }

                    Status.ERROR -> {
                        NotificationHandle.showErrorSnackBar(
                            requireView(), resource.message.toString()
                        )
                    }
                }
            }
    }

    private fun setupNavigation() {
        binding.cardManageAdmin.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN, findNavController()
            )
        }
    }

    private fun setupAdapterManageAdminAndSuperAdmin(data: ManageAdminAndSuperAdminResponse?) {
        val adapter = ManageAdminAdapter().apply {
            submitList(data?.data)
            callbackOnEditClickListener { user ->
                Navigation.navigateFragment(Destination.MANAGE_ADMIN_TO_ADD_OR_UPDATE_ADMIN,
                    findNavController(),
                    Bundle().apply { putParcelable(Key.BUNDLE_DATA_ADMIN_OR_SUPER_ADMIN, user) })
            }
            callbackOnDeleteClickListener { id ->
                notifyDataSetChanged()
                setupDeleteDataAdminOrSuperAdmin(id)
            }
        }

        binding.cardManageAdmin.recyclerView.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

}