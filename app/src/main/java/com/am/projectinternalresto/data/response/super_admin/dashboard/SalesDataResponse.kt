package com.am.projectinternalresto.data.response.super_admin.dashboard

import com.google.gson.annotations.SerializedName

data class SalesDataResponse(

    @field:SerializedName("data")
    val data: DataItemSales? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class YearlyRevenue(

    @field:SerializedName("1")
    val january: Int? = null,

    @field:SerializedName("2")
    val february: Int? = null,

    @field:SerializedName("3")
    val march: Int? = null,

    @field:SerializedName("4")
    val april: Int? = null,

    @field:SerializedName("5")
    val may: Int? = null,

    @field:SerializedName("6")
    val june: Int? = null,

    @field:SerializedName("7")
    val july: Int? = null,

    @field:SerializedName("8")
    val august: Int? = null,

    @field:SerializedName("9")
    val september: Int? = null,

    @field:SerializedName("10")
    val october: Int? = null,

    @field:SerializedName("11")
    val november: Int? = null,

    @field:SerializedName("12")
    val december: Int? = null
)

data class DataItemSales(

    @field:SerializedName("pendapatanBulanIni")
    val currentMonthRevenue: Int? = null,

    @field:SerializedName("pendapatanTahunIni")
    val yearlyRevenue: YearlyRevenue? = null,

    @field:SerializedName("totalPenjualanHariIni")
    val todayTotalSales: Int? = null,

    @field:SerializedName("pendapatanHariIni")
    val todayRevenue: Int? = null,

    @field:SerializedName("pesananOnline")
    val onlineOrders: Int? = null,

    @field:SerializedName("pesananOffline")
    val offlineOrders: Int? = null
)
