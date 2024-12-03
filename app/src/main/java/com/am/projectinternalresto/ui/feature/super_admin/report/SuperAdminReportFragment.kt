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
import com.am.projectinternalresto.data.response.super_admin.report.DataItemReport
import com.am.projectinternalresto.data.response.super_admin.report.ListReportResponse
import com.am.projectinternalresto.databinding.FragmentReportBinding
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.report.ManageReportAdapter
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertFilterPrintReport
import com.am.projectinternalresto.ui.widget.dialog_fragment.DetailReportDialogFragment
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.generatePDFReport
import org.koin.android.ext.android.inject
import java.util.Calendar


class SuperAdminReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageReportViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val locationViewModel: LocationViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private var currentLocationId: String? = null
    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
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
            showAlertFilterPrintReport(
                requireContext(),
                locationViewModel,
                token
            ) { locationId, month, year ->
                currentLocationId = locationId
                currentMonth = month
                currentYear = year
//                setupGeneratePdf(locationId, initialDate, deadlineDate)
            }
//            showAlertFilterReportByLocation(
//                requireContext(),
//                locationViewModel,
//                token
//            ) { locationId ->
//
//                setupFilterReport(locationId)
//            }
        }
        binding.cardReport.buttonPrint.setOnClickListener {
            showAlertFilterPrintReport(
                requireContext(),
                locationViewModel,
                token
            ) { locationId, initialDate, deadlineDate ->
                Log.e("CheckPrint", "id : $locationId | $initialDate | $deadlineDate")
//                setupGeneratePdf(locationId, initialDate, deadlineDate)
            }
        }

        binding.cardReport.buttonDelete.setOnClickListener {
            if (currentLocationId != null) {
                Log.e(
                    "Check",
                    "check delete data : $currentLocationId | $currentMonth | $currentYear"
                )
                setupDeleteDataReport(currentLocationId.toString(), currentMonth, currentYear)
            } else {
                showAlertFilterPrintReport(
                    requireContext(),
                    locationViewModel,
                    token
                ) { locationId, month, year ->
                    Log.e("Check", "check delete data : $locationId | $month | $year")
                    setupDeleteDataReport(locationId, month, year)
                }
            }
        }
    }

    private fun setupDeleteDataReport(locationId: String, month: Int, year: Int) {
        viewModel.deleteReportSuperAdmin(token, locationId, month, year)
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
                        NotificationHandle.showErrorSnackBar(
                            requireView(),
                            result.message.toString()
                        )
                        Log.e("Check", "error : $result.message")
                    }
                }
            }
    }

    private fun setupGeneratePdf(locationId: String, initialDate: String, deadlineDate: String) {
        viewModel.getDataReportForPrint(token, locationId, initialDate, deadlineDate)
            .observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        val data = result.data?.data as? List<DataItemReport>
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

    private fun setupFilterReport(id: String) {
        viewModel.filterReportByLocation(token, id).observe(viewLifecycleOwner) { result ->
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