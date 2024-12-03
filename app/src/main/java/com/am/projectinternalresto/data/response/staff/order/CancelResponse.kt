package com.am.projectinternalresto.data.response.staff.order

import com.google.gson.annotations.SerializedName

data class CancelResponse(

    @field:SerializedName("data")
    val data: DataItemListOrder? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
