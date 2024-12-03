package com.am.projectinternalresto.ui.widget.dialog_fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.FragmentFilterSalesDialogBinding
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import org.koin.android.ext.android.inject

class FilterSalesDialogFragment : DialogFragment() {
    private var _binding: FragmentFilterSalesDialogBinding? = null
    private val binding get() = _binding!!
    private val locationViewModel: LocationViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterSalesDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            // Dapatkan ukuran layar
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels

            // Set layout params
            setLayout(
                (screenWidth * 0.5).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.gravity = Gravity.CENTER
        }


        binding.iconClose.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            val filterSalesDialogFragment = FilterSalesDialogFragment()
            filterSalesDialogFragment.show(fragmentManager, filterSalesDialogFragment.tag)
        }
    }
}