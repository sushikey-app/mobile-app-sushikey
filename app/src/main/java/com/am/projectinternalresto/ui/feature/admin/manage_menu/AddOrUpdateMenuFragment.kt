package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateMenuBinding

class AddOrUpdateMenuFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateMenuBinding.inflate(inflater, container, false)
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}