package com.am.projectinternalresto.ui.feature.super_admin.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.projectinternalresto.data.response.super_admin.report.ListReportResponse
import com.am.projectinternalresto.databinding.FragmentReportBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.report.ManageReportAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertFilterAndPrintReportSuperAdmin
import com.am.projectinternalresto.ui.widget.alert.showConfirmDeleteReportAlert
import com.am.projectinternalresto.ui.widget.dialog_fragment.DetailReportDialogFragment
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.generatePDFReport
import org.koin.android.ext.android.inject


class SuperAdminReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageReportViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val locationViewModel: LocationViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private var currentLocationId: String? = null
    private var currentStartDate: Int = 0
    private var currentStartMonth: Int = 0
    private var currentStartYear: Int = 0
    private var currentEndDate: Int = 0
    private var currentEndMonth: Int = 0
    private var currentEndYear: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        setupGetDataReport()
        setupNavigation()
        return binding.root
    }

    private fun setupNavigation() {
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataReport() }
        binding.buttonNavigateToCancel.setOnClickListener {
            Navigation.navigateFragment(
                Destination.MANAGE_REPORT_TO_CANCEL_ORDER,
                findNavController()
            )
        }
        binding.cardReport.buttonFilter.setOnClickListener {
            showAlertFilterAndPrintReportSuperAdmin(
                requireContext(),
                locationViewModel,
                token
            ) { locationId, startDate, startMonth, startYear, endDate, endMonth, endYear ->
                setupFilterReport(
                    locationId, startDate, startMonth, startYear, endDate, endMonth, endYear
                )
                Log.e(
                    "CheckPrint",
                    "id : $locationId | $startDate | $startMonth | $endDate | $endMonth"
                )
                currentLocationId = locationId
                currentStartDate = startDate
                currentStartMonth = startMonth
                currentStartYear = startYear
                currentEndDate = endDate
                currentEndMonth = endMonth
                currentEndYear = endYear
            }
        }
        binding.cardReport.buttonPrint.setOnClickListener {
            if (currentLocationId != null) {
                setupGeneratePdf(
                    currentLocationId.toString(),
                    currentStartDate,
                    currentStartMonth,
                    currentStartYear,
                    currentEndDate,
                    currentEndMonth,
                    currentEndYear
                )
            } else {
                showAlertFilterAndPrintReportSuperAdmin(
                    requireContext(),
                    locationViewModel,
                    token
                ) { locationId, startDate, startMonth, startYear, endDate, endMonth, endYear ->
                    Log.e(
                        "CheckPrint",
                        "id : $locationId | $locationId | $startDate | $startMonth | $endDate | $endMonth"
                    )
                    setupGeneratePdf(
                        locationId,
                        startDate,
                        startMonth,
                        startYear,
                        endDate,
                        endMonth,
                        endYear
                    )
                }
            }

            binding.cardReport.buttonDelete.setOnClickListener {
                if (currentLocationId != null) {
                    showConfirmDeleteReportAlert(requireContext()) {
                        setupDeleteDataReport(
                            currentLocationId.toString(),
                            currentStartDate,
                            currentStartMonth,
                            currentStartYear,
                            currentEndDate,
                            currentEndMonth,
                            currentEndYear
                        )
                    }
                } else {
                    showAlertFilterAndPrintReportSuperAdmin(
                        requireContext(),
                        locationViewModel,
                        token
                    ) { locationId, startDate, startYear, startMonth, endDate, endMonth, endYear ->
                        Log.e(
                            "CheckPrint",
                            "id : $locationId | $locationId | $startDate | $startMonth | $startYear | $endDate | $endMonth | $endYear"
                        )
                        showConfirmDeleteReportAlert(requireContext()) {
                            setupDeleteDataReport(
                                locationId,
                                startDate, startMonth, startYear, endDate, endMonth, endYear
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setupFilterReport(
        locationId: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) {
        viewModel.getDataFilter(
            token, locationId, startDate, startMonth, startYear, endDate, endMonth, endYear
        )
            .observe(viewLifecycleOwner) { result ->
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
                        NotificationHandle.showErrorSnackBar(
                            requireView(),
                            result.message.toString()
                        )
                    }
                }
            }
    }

    private fun setupDeleteDataReport(
        locationId: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) {
        viewModel.deleteReportSuperAdmin(
            token, locationId, startDate, startMonth, startYear, endDate, endMonth, endYear
        )
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {
                        ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                            binding.cardReport.shimmerLayout,
                            true
                        )
                    }

                    Status.SUCCESS -> {
                        NotificationHandle.showSuccessSnackBar(
                            requireView(),
                            result.data?.message.toString()
                        )
                        setupGetDataReport()
                    }

                    Status.ERROR -> {
                        ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                            binding.cardReport.shimmerLayout,
                            false
                        )
                        NotificationHandle.showErrorSnackBar(
                            requireView(),
                            result.message.toString()
                        )
                        Log.e("Check", "error : $result.message")
                    }
                }
            }
    }


    private fun setupGeneratePdf(
        locationId: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) {
        viewModel.getDataReportForPrint(
            token, locationId, startDate, startMonth, startYear, endDate, endMonth, endYear
        )
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        val data = result.data
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
        viewModel.getReportOrder(token).observe(viewLifecycleOwner) { result ->
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
        val adapter = ManageReportAdapter(true).apply {
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