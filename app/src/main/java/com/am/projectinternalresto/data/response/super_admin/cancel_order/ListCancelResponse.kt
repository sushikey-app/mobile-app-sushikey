package com.am.projectinternalresto.data.response.super_admin.cancel_order

import com.google.gson.annotations.SerializedName

data class ListCancelResponse(

    @field:SerializedName("data")
    val data: List<DataItemCancelOrder?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemCancelOrder(

    @field:SerializedName("tanggal_pemesanan")
    val tanggalPemesanan: String? = null,

    @field:SerializedName("lokasi")
    val lokasi: Lokasi? = null,

    @field:SerializedName("nomor_order")
    val nomorOrder: String? = null,

    @field:SerializedName("total_harga")
    val totalHarga: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("status_pesanan")
    val statusPesanan: String? = null
)

data class Lokasi(

    @field:SerializedName("lokasi")
    val lokasi: String? = null,

    @field:SerializedName("nama_resto")
    val namaResto: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("nomor_telephone")
    val nomorTelephone: String? = null,

    )
