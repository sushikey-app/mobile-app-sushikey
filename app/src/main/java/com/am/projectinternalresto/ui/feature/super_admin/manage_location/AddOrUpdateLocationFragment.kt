package com.am.projectinternalresto.ui.feature.super_admin.manage_location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.dummy.DummyData
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateLocationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddOrUpdateLocationFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateLocationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateLocationBinding.inflate(inflater, container, false)
        setupNavigation()

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupNavigation() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.edtLocation.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                setupDialog()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    private fun setupDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Select Location")
        val dataDummy = DummyData.dummyDataSelectLocation
        val onlyNameLocation = dataDummy.map { it.dataLocation.restoName }.toTypedArray()
        val onlyLocationChecked = dataDummy.map { it.isChecked }.toBooleanArray()

        builder.setMultiChoiceItems(
            onlyNameLocation,
            onlyLocationChecked
        ) { _, position, isChecked ->
            dataDummy[position].isChecked = isChecked
        }

        builder.setPositiveButton("Oke") { _, _ ->
            val checked = dataDummy.filter { it.isChecked }.map { it.dataLocation.restoName }
            Toast.makeText(requireContext(), "IsChecked : $checked", Toast.LENGTH_SHORT).show()
        }

         builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}