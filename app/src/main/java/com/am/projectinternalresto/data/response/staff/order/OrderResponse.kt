package com.am.projectinternalresto.data.response.staff.order

import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
import com.google.gson.annotations.SerializedName

data class OrderResponse(

    @field:SerializedName("data")
    val data: DataItemOrder? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemOrder(

    @field:SerializedName("pembayaran")
    val payment: Payment? = null,

    @field:SerializedName("pesanan")
    val order: List<OrderItem?>? = null,

    @field:SerializedName("nama_lokasi")
    val locationName: String? = null,

    @field:SerializedName("nama_kasir")
    val staffName: String? = null,

    @field:SerializedName("alasan_pembatalan")
    val reasonCancelOrder: String? = null,
)

data class Payment(

    @field:SerializedName("nama_pembeli")
    val buyerName: String? = null,

    @field:SerializedName("uang_dibayarkan")
    val moneyPaid: Int? = null,

    @field:SerializedName("metode")
    val paymentMethod: String? = null,

    @field:SerializedName("status_pembayaran")
    val paymentStatus: String? = null,

    @field:SerializedName("nomor_order")
    val noOrder: String? = null,

    @field:SerializedName("total_harga")
    val totalPrice: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type_pesanan")
    val typeOrder: String? = null,

    @field:SerializedName("status_pesanan")
    val statusOrder: String? = null,

    @field:SerializedName("subtotal")
    val subtotal: Int? = null,

    @field:SerializedName("harga_diskon")
    val disc: Int? = null,
    )

data class OrderItem(

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("qty")
    val qty: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("menu")
    val menu: Menu? = null,

    @field:SerializedName("topping")
    val topping: List<ToppingItem?>? = null,
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
    val lokasiId: String? = null,

    @field:SerializedName("harga_diskon")
    val discPrice: Int? = null,

    @field:SerializedName("diskon")
    val disc: Int? = null,
)

