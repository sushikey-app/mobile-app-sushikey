package com.am.projectinternalresto.ui.feature.admin.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.super_admin.report.DataItemReport
import com.am.projectinternalresto.data.response.super_admin.report.ListReportResponse
import com.am.projectinternalresto.databinding.FragmentReportBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.report.ManageReportAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import com.am.projectinternalresto.ui.feature.super_admin.report.ManageReportViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertFilterPrintReport
import com.am.projectinternalresto.ui.widget.dialog_fragment.DetailReportDialogFragment
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.generatePDFReport
import org.koin.android.ext.android.inject

class AdminReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageReportViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val locationViewModel: LocationViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        setupNavigation()
        setupView()
        setupGetDataReport()
        return binding.root
    }

    private fun setupView() {
        binding.buttonNavigateToCancel.visibility = View.GONE
        binding.cardReport.buttonDelete.visibility = View.GONE
    }

    private fun setupNavigation() {
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataReport() }
        binding.cardReport.buttonPrint.setOnClickListener {
            showAlertFilterPrintReport(
                requireContext(),
                locationViewModel,
                token
            ) { locationId, initialDate, deadlineDate ->
//                setupGeneratePdf(locationId, initialDate, deadlineDate)
            }
        }
    }


    private fun setupGeneratePdf(locationId: String, initialDate: String, deadlineDate: String) {
        viewModel.getDataReportForPrintAdmin(token, locationId, initialDate, deadlineDate)
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        val data = result.data?.data as? List<DataItemReport>
                        Log.e(
                            "Check", "Print report data : $data" +
                                    ""
                        )
                        if (data != null) {
                            val pdfUri = generatePDFReport(requireContext(), data)
                            if (pdfUri != null) {
                                NotificationHandle.showSuccessSnackBar(
                                    requireView(),
                                    "PDF Berhasil Dibuat"
                                )

                                openPdf(pdfUri)
                            } else {
                                NotificationHandle.showErrorSnackBar(
                                    requireView(),
                                    "Gagal membuat pdf"
                                )
                            }
                        } else {
                            NotificationHandle.showErrorSnackBar(
                                requireView(),
                                "Tidak ada data dari server"
                            )
                        }
                    }

                    Status.ERROR -> {
                        NotificationHandle.showErrorSnackBar(
                            requireView(),
                            result.message.toString()
                        )
                    }
                }
            }
    }

    private fun setupGetDataReport() {
        viewModel.getReportAdmin(token).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.cardReport.shimmerLayout,
                        true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.cardReport.shimmerLayout,
                        false
                    )
                    binding.swipeRefreshLayout.isRefreshing = false
                    setupAdapter(result.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.cardReport.shimmerLayout,
                        false
                    )
                    binding.swipeRefreshLayout.isRefreshing = false
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapter(data: ListReportResponse?) {
        val adapter = ManageReportAdapter().apply {
            submitList(data?.data)
            callbackOnclickToDetail { id ->
                DetailReportDialogFragment.show(childFragmentManager, id)
            }
        }
        binding.cardReport.recyclerViewContentTableLocation.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openPdf(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }
}