package com.am.projectinternalresto.ui.widget.dialog_fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.response.super_admin.report.DataItemDetailReport
import com.am.projectinternalresto.databinding.CustomLayourDialogDetailReportOrderBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.report.DetailReportOrderAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.report.ManageReportViewModel
import com.am.projectinternalresto.utils.NotificationHandle
import org.koin.android.ext.android.inject

class DetailReportDialogFragment(private val idOrderReport: String) : DialogFragment() {
    private var _binding: CustomLayourDialogDetailReportOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageReportViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomLayourDialogDetailReportOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            setLayout(
                (screenWidth * 0.7).toInt(),
                (screenHeight * 0.7).toInt()
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.gravity = Gravity.CENTER
        }

        getDataDetail()
        binding.buttonBack.setOnClickListener { dismiss() }
    }

    private fun getDataDetail() {
        viewModel.getDetailReport(token, idOrderReport).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    setupAdapter(result.data?.data)
                }

                Status.ERROR -> {
                    Log.e("Check", "error : ${result.message}")
                    NotificationHandle.showErrorSnackBar(
                        requireView(),
                        result.message.toString()
                    )
                }
            }
        }
    }

    private fun setupAdapter(data: DataItemDetailReport?) {
        val adapter = DetailReportOrderAdapter().apply {
            submitList(data?.pesanan)
        }
        binding.recyclerViewItem.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }


    companion object {
        fun show(fragmentManager: FragmentManager, id: String) {
            val view = DetailReportDialogFragment(id)
            view.show(fragmentManager, view.tag)
        }
    }
}