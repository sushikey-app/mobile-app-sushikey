package com.am.projectinternalresto.data.response.staff.order

import com.google.gson.annotations.SerializedName

data class OrderDoneResponse(

    @field:SerializedName("data")
    val data: OrderItem? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)