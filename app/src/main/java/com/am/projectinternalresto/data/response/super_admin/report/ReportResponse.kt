package com.am.projectinternalresto.data.response.super_admin.report

import com.google.gson.annotations.SerializedName

data class ReportResponse(

    @field:SerializedName("data")
    val data: List<DataItemReport?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class Lokasi(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("lokasi")
    val lokasi: String? = null,

    @field:SerializedName("nama_resto")
    val namaResto: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Any? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("nomor_telephone")
    val nomorTelephone: String? = null
)

data class DataItemReport(

    @field:SerializedName("nama_pembeli")
    val namaPembeli: String? = null,

    @field:SerializedName("metode")
    val metode: String? = null,

    @field:SerializedName("tanggal_pemesanan")
    val tanggalPemesanan: String? = null,

    @field:SerializedName("status_pembayaran")
    val statusPembayaran: String? = null,

    @field:SerializedName("lokasi")
    val lokasi: Lokasi? = null,

    @field:SerializedName("nomor_order")
    val nomorOrder: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type_pesanan")
    val typePesanan: String? = null,

    @field:SerializedName("pesanan")
    val pesanan: List<PesananItem?>? = null,

    @field:SerializedName("status_pesanan")
    val statusPesanan: String? = null
)

data class Menu(

    @field:SerializedName("komposisi")
    val komposisi: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("kategori_id")
    val kategoriId: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("kuota")
    val kuota: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("nomor_menu")
    val nomorMenu: String? = null,

    @field:SerializedName("lokasi_id")
    val lokasiId: String? = null
)

data class PesananItem(

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("qty")
    val qty: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("menu")
    val menu: Menu? = null,

    @field:SerializedName("menu_id")
    val menuId: String? = null
)
