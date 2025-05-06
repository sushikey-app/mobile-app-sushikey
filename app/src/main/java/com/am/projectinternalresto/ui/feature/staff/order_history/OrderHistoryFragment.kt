package com.am.projectinternalresto.ui.feature.staff.order_history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.am.projectinternalresto.ui.feature.staff.order_menu.ManageOrderMenuViewModel
import com.am.projectinternalresto.ui.feature.super_admin.report.ManageReportViewModel
import com.am.projectinternalresto.ui.widget.alert.showAlertFilterAdminAndStaff
import com.am.projectinternalresto.ui.widget.dialog_fragment.DetailReportDialogFragment
import com.am.projectinternalresto.utils.Destination
import com.am.projectinternalresto.utils.Navigation
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.generatePDFReport
import org.koin.android.ext.android.inject

class OrderHistoryFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageReportViewModel by inject()
    private val orderViewModel: ManageOrderMenuViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }

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
        setupNavigation()
        setupView()
        setupGetDataReport()
        return binding.root
    }

    private fun setupView() {
        binding.cardReport.buttonDelete.visibility = View.GONE
        binding.cardReport.textLocation.visibility = View.GONE
    }

    private fun setupNavigation() {
        binding.swipeRefreshLayout.setOnRefreshListener { setupGetDataReport() }
        binding.buttonNavigateToCancel.setOnClickListener {
            Navigation.navigateFragment(
                Destination.HISTORY_ORDER_STAFF_TO_CANCEL_ORDER_STAFF,
                findNavController()
            )
        }
        binding.cardReport.buttonFilter.setOnClickListener {
            showAlertFilterAdminAndStaff(requireContext()) { startDate, startMonth, startYear, endDate, endMonth, endYear ->
                setupFilterReport(startDate, startMonth, startYear, endDate, endMonth, endYear)
                currentStartDate = startDate
                currentStartMonth = startMonth
                currentStartYear = startYear
                currentEndDate = endDate
                currentEndMonth = endMonth
                currentEndYear = endYear
            }
        }
        binding.cardReport.buttonPrint.setOnClickListener {
            if (currentStartDate != 0) {
                // print data
                setupGeneratePdf(
                    currentStartDate,
                    currentStartMonth,
                    currentStartYear,
                    currentEndDate,
                    currentEndMonth,
                    currentEndYear
                )
            } else {
                showAlertFilterAdminAndStaff(
                    requireContext(),
                ) { startDate, startMonth, startYear, endDate, endMonth, endYear ->
                    setupGeneratePdf(startDate, startMonth, startYear, endDate, endMonth, endYear)
                }
            }
        }

    }


    private fun setupFilterReport(
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) {
        viewModel.getDataFilterAdmin(
            token, startDate, startMonth, startYear, endDate, endMonth, endYear
        ).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.cardReport.shimmerLayout, true
                    )
                }

                Status.SUCCESS -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.cardReport.shimmerLayout, false
                    )
                    binding.swipeRefreshLayout.isRefreshing = false
                    setupAdapter(result.data)
                }

                Status.ERROR -> {
                    ProgressHandle.setupVisibilityShimmerLoadingInLinearLayout(
                        binding.cardReport.shimmerLayout, false
                    )
                    binding.swipeRefreshLayout.isRefreshing = false
                    NotificationHandle.showErrorSnackBar(
                        requireView(), result.message.toString()
                    )
                }
            }
        }
    }


    private fun setupGeneratePdf(
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) {
        viewModel.getDataReportForPrintAdmin(
            token, startDate, startMonth, startYear, endDate, endMonth, endYear
        ).observe(viewLifecycleOwner) { result ->
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

    private fun setupCancelOrder(id: String) {
        orderViewModel.cancelOrder(token, id).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.LOADING -> {}

                Status.SUCCESS -> {
                    NotificationHandle.showSuccessSnackBar(
                        requireView(),
                        result.data?.message.toString()
                    )
                    (binding.cardReport.recyclerViewContentTableLocation.adapter as? ManageReportAdapter)?.submitList(
                        emptyList()
                    )
                    setupGetDataReport()
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
                }
            }
        }
    }

    private fun setupAdapter(data: ListReportResponse?) {
        val adapter = ManageReportAdapter(false).apply {
            submitList(data?.data)
            callbackOnclickToDetail { id ->
                DetailReportDialogFragment.show(childFragmentManager, id)
            }
            callbackOnclickCancelOrders { id ->
                setupCancelOrder(id)
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
