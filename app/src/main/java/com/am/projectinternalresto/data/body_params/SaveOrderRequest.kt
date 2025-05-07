package com.am.projectinternalresto.data.body_params

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("nama_pembeli") val nameBuyer: String,
    @SerializedName("metode") val methodPayment: String,
    @SerializedName("uang_dibayarkan") val totalPaid: Int? = null,
    @SerializedName("pesanan") val order: List<OrderItemRequest>,
    @SerializedName("type_diskon") val typeDisc: String,
    @SerializedName("diskon_pembayaran") val disc: Int?,
)

data class SaveOrderRequest(
    @SerializedName("nama_pembeli") val nameCustomer: String,
    @SerializedName("pesanan") val order: List<OrderItemRequest>
)

data class OrderItemRequest(
    @SerializedName("menu_id") val menuId: String,
    @SerializedName("qty") val qty: Int,
    @SerializedName("note") val note: String?,
    @SerializedName("topping") val topping: List<ToppingItemRequest>?
)

data class ToppingItemRequest(
    @SerializedName("id") val id: String
)

