package com.am.projectinternalresto.data.body_params

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("metode") val methodPayment: String,
    @SerializedName("uang_dibayarkan") val totalPaid: Int? = null,
    @SerializedName("type_diskon") val typeDisc: String,
    @SerializedName("diskon_pembayaran") val disc: Int?,
)
