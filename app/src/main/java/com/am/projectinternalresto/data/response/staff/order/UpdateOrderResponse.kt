package com.am.projectinternalresto.data.response.staff.order

import com.google.gson.annotations.SerializedName

data class UpdateOrderResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class MenuUpdated(

    @field:SerializedName("note")
    val note: Any? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("qty")
    val qty: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("harga_pesanan")
    val hargaPesanan: Int? = null,

    @field:SerializedName("menu_id")
    val menuId: String? = null
)

data class Data(

    @field:SerializedName("pembayaran")
    val pembayaran: Pembayaran? = null,

    @field:SerializedName("pesanan")
    val pesanan: List<PesananItem?>? = null
)

data class PesananItem(

    @field:SerializedName("menu")
    val menu: MenuUpdated? = null,

    @field:SerializedName("topping")
    val topping: List<ToppingItem?>? = null
)

data class Pembayaran(

    @field:SerializedName("kembalian")
    val kembalian: Int? = null,

    @field:SerializedName("nama_pembeli")
    val namaPembeli: String? = null,

    @field:SerializedName("uang_dibayarkan")
    val uangDibayarkan: Int? = null,

    @field:SerializedName("nomor_order")
    val nomorOrder: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("total_harga")
    val totalHarga: Int? = null,

    @field:SerializedName("lokasi_id")
    val lokasiId: String? = null,

    @field:SerializedName("status_pesanan")
    val statusPesanan: String? = null,

    @field:SerializedName("status_pembatalan")
    val statusPembatalan: Any? = null,

    @field:SerializedName("metode")
    val metode: String? = null,

    @field:SerializedName("type_diskon")
    val typeDiskon: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("diskon_pembayaran")
    val diskonPembayaran: Int? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("status_pembayaran")
    val statusPembayaran: String? = null,

    @field:SerializedName("subtotal")
    val subtotal: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("harga_diskon")
    val hargaDiskon: Int? = null
)
