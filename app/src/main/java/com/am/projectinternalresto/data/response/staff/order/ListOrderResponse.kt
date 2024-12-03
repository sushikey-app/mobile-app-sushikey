package com.am.projectinternalresto.data.response.staff.order

import com.google.gson.annotations.SerializedName

data class ListOrderResponse(

    @field:SerializedName("data")
    val data: List<DataItemListOrder?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemListOrder(

    @field:SerializedName("nama_pembeli")
    val nameBuyer: String? = null,

    @field:SerializedName("metode")
    val methodPayment: String? = null,

    @field:SerializedName("uang_dibayarkan")
    val totalPaid: Int? = null,

    @field:SerializedName("status_pembayaran")
    val statusPayment: String? = null,

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
)
