package com.am.projectinternalresto.ui.feature.admin.manage_category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.params.CategoryBody
import com.am.projectinternalresto.data.response.admin.category.AddOrUpdateCategoryResponse
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateCategoryBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import org.koin.android.ext.android.inject

class AddOrUpdateCategoryMenuFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageCategoryViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private val dataCategory: DataItemCategory? by lazy { arguments?.getParcelable(Key.BUNDLE_DATA_CATEGORY) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateCategoryBinding.inflate(inflater, container, false)
        setupDisplay()
        setupNavigation()
        return binding.root
    }

    private fun setupDisplay() {
        UiHandle.setupDisableHintForField(binding.edlCodeCategory, binding.edlNameCategory)
        binding.buttonSave.text = getString(R.string.save_data)
        if (dataCategory != null) {
            binding.edtCodeCategory.setText(dataCategory?.codeCategory)
            binding.edtNameCategory.setText(dataCategory?.nameCategory)
            binding.actionHeadline.textHeadline.text = getString(R.string.edit_data)
        } else {
            binding.actionHeadline.textHeadline.text = getString(R.string.add_category)
        }
    }

    private fun setupNavigation() {
        binding.actionHeadline.buttonBack.setOnClickListener { findNavController().popBackStack() }
        binding.buttonSave.setOnClickListener {
            if (dataCategory != null) {
                setupPutDataCategoryToApi()
            } else {
                setupPostDataCategoryToApi()
            }
        }
    }


    private fun setupPutDataCategoryToApi() {
        viewModel.updateCategoryMenu(token, dataCategory?.id.toString(), dataCategory())
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result)
            }
    }

    private fun setupPostDataCategoryToApi() {
        viewModel.addCategoryMenu(token, dataCategory())
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result)
            }
    }

    private fun handleApiStatus(
        result: Resource<AddOrUpdateCategoryResponse?>,
    ) {
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
                findNavController().popBackStack()
                NotificationHandle.showSuccessSnackBar(
                    requireView(),
                    result.data?.message.toString()
                )
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false
                )
                NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    private fun dataCategory(): CategoryBody {
        return CategoryBody(
            nameCategory = binding.edtNameCategory.text.toString(),
            codeCategory = binding.edtCodeCategory.text.toString()
        )
    }
}