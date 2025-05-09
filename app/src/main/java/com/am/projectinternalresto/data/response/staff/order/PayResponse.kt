package com.am.projectinternalresto.data.response.staff.order


import com.google.gson.annotations.SerializedName

data class PayResponse(

    @field:SerializedName("data")
    val data: DataItemPay? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class OrderPayItems(

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("qty")
    val qty: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("nomor_menu")
    val nomorMenu: String? = null,

    @field:SerializedName("topping")
    val topping: List<ToppingItem?>? = null,

    @field:SerializedName("harga_pesanan")
    val hargaPesanan: Int? = null
)

data class PaymentItem(

    @field:SerializedName("nama_pembeli")
    val namaPembeli: String? = null,

    @field:SerializedName("metode")
    val metode: String? = null,

    @field:SerializedName("uang_dibayarkan")
    val uangDibayarkan: Int? = null,

    @field:SerializedName("status_pembayaran")
    val statusPembayaran: String? = null,

    @field:SerializedName("nomor_order")
    val nomorOrder: String? = null,

    @field:SerializedName("total_harga")
    val totalHarga: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("lokasi_id")
    val lokasiId: String? = null,

    @field:SerializedName("type_pesanan")
    val typePesanan: String? = null,

    @field:SerializedName("status_pesanan")
    val statusPesanan: String? = null,

    @field:SerializedName("subtotal")
    val subtotal: Int,

    @field:SerializedName("harga_diskon")
    val totalDisc: Int
)

data class DataItemPay(

    @field:SerializedName("nama_lokasi")
    val locationName: String? = null,

    @field:SerializedName("pembayaran")
    val payment: PaymentItem? = null,

    @field:SerializedName("nama_kasir")
    val staffName: String? = null,

    @field:SerializedName("pesanan")
    val orderItems: List<OrderPayItems?>? = null
)

data class ToppingItem(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    )
