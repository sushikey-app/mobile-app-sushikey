package com.am.projectinternalresto.ui.widget.dialog_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.am.projectinternalresto.databinding.FragmentFilterSalesDialogBinding

class FilterSalesDialogFragment : DialogFragment() {
    private var _binding: FragmentFilterSalesDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterSalesDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {
        fun show(fragmentManager: FragmentManager) {
            val filterSalesDialogFragment = FilterSalesDialogFragment()
            filterSalesDialogFragment.show(fragmentManager, filterSalesDialogFragment.tag)
        }
    }
}