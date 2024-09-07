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
    val order: List<OrderItem?>? = null
)

data class Payment(

    @field:SerializedName("nama_pembeli")
    val buyerName: String? = null,

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
    val statusOrder: String? = null
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

    @field:SerializedName("topping")
    val topping: List<ToppingItem?>? = null,
)

