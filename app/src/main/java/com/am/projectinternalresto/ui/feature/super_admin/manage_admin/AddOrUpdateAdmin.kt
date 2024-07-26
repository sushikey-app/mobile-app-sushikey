package com.am.projectinternalresto.ui.feature.super_admin.manage_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateAdminBinding

class AddOrUpdateAdmin : Fragment() {
    private var _binding: FragmentAddOrUpdateAdminBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateAdminBinding.inflate(inflater, container, false)
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}