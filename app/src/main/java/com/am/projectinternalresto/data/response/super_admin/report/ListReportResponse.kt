package com.am.projectinternalresto.data.response.super_admin.report

import com.google.gson.annotations.SerializedName

data class ListReportResponse(

    @field:SerializedName("data")
    val data: List<DataItemListReport?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemListReport(

    @field:SerializedName("tanggal_pemesanan")
    val dateOrder: String? = null,

    @field:SerializedName("nomor_order")
    val noOrder: String? = null,

    @field:SerializedName("total_harga")
    val totalPrice: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("status_pesanan")
    val statusOrder: String? = null,

    @field:SerializedName("lokasi")
    val location: LocationItem? = null,
)

data class LocationItem(

    @field:SerializedName("lokasi")
    val lokasiResto: String? = null,

    @field:SerializedName("nama_resto")
    val namaResto: String? = null
)
