package com.am.projectinternalresto.ui.feature.admin.manage_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentManageCategoryBinding
import com.am.projectinternalresto.ui.adapter.manage_category.ManageCategoryAdapter
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.UiHandler

class ManageCategoryFragment : Fragment() {
    private var _binding: FragmentManageCategoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCategoryBinding.inflate(inflater, container, false)
        setupManageCategoryAdapter()
        setupNavigation()
        return binding.root
    }

    private fun setupManageCategoryAdapter() {
        val adapter = ManageCategoryAdapter().apply {
            submitList(DummyData.dataDummyCategory)
            callbackOnClickEdit = {
                val bundle = Bundle().apply {
                    putBoolean(KEY_UPDATE_CATEGORY, false)
                }
                Navigation.navigateFragment(
                    Destination.MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY,
                    findNavController(),
                    bundle
                )
            }
            callbackOnClickDelete = {
                UiHandler.toastErrorMessage(requireContext(), "Delete Category")
            }
        }
        binding.cardManageCategory.recyclerViewCategory.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupNavigation() {
        /*add new category*/
        binding.cardManageCategory.buttonAdd.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_CATEGORY_TO_ADD_OR_UPDATE_CATEGORY,
                findNavController()
            )
        }
    }

    companion object {
        const val KEY_UPDATE_CATEGORY = "key_update_category"
    }
}