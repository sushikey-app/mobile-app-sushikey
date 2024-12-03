package com.am.projectinternalresto.ui.feature.super_admin.report

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.service.source.ReportRepository

class ManageReportViewModel(private val repository: ReportRepository) : ViewModel() {
    fun getReportOrder(token: String) = repository.getListDataReportSuperAdmin(token)
    fun getDataReportForPrint(
        token: String,
        locationId: String,
        initialDate: String,
        deadlineDate: String
    ) = repository.getDataReportForPrintSuperAdmin(token, locationId, initialDate, deadlineDate)

    fun getDetailReport(token: String, id: String) =
        repository.getDetailDataReportSuperAdmin(token, id)

    fun getReportAdmin(token: String) = repository.getListDataReportAdmin(token)
    fun getDataReportForPrintAdmin(
        token: String,
        locationId: String,
        initialDate: String,
        deadlineDate: String
    ) = repository.getDataReportForPrintAdmin(token, locationId, initialDate, deadlineDate)

    fun getDetailReportAdmin(token: String, id: String) =
        repository.getDetailDataReportSuperAdmin(token, id)

    fun filterReportByLocation(token: String, locationId: String) =
        repository.filterReportByLocation(token, locationId)

    fun deleteReportSuperAdmin(token: String, locationId: String, month: Int, years: Int) =
        repository.deleteReportSuperAdmin(token, locationId, month, years)
}