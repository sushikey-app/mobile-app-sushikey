package com.am.projectinternalresto.ui.feature.super_admin.report

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.service.source.ReportRepository

class ManageReportViewModel(private val repository: ReportRepository) : ViewModel() {
    fun getReportOrder(token: String) = repository.getListDataReportSuperAdmin(token)
    fun getDataReportForPrint(
        token: String,
        locationId: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int,
        endYear: Int
    ) = repository.getDataReportForPrintSuperAdmin(
        token,
        locationId,
        startDate, startMonth, startYear, endDate, endMonth, endYear
    )

    fun getDataFilter(
        token: String,
        locationId: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) = repository.getDataFilterReportSuperAdmin(
        token, locationId, startDate, startMonth, startYear, endDate, endMonth, endYear
    )

    fun getDataFilterAdmin(
        token: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) = repository.getDataFilterReportAdmin(
        token,
        startDate, startMonth, startYear, endDate, endMonth, endYear
    )

    fun getDetailReport(token: String, id: String) =
        repository.getDetailDataReportSuperAdmin(token, id)

    fun getReportAdmin(token: String) = repository.getListDataReportAdmin(token)
    fun getDataReportForPrintAdmin(
        token: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) = repository.getDataReportForPrintAdmin(
        token, startDate, startMonth, startYear, endDate, endMonth, endYear
    )


    fun deleteReportSuperAdmin(
        token: String, locationId: String,
        startDate: Int, startMonth: Int, startYear: Int, endDate: Int, endMonth: Int, endYear: Int
    ) =
        repository.deleteReportSuperAdmin(
            token,
            locationId,
            startDate, startMonth, startYear, endDate, endMonth, endYear
        )
}