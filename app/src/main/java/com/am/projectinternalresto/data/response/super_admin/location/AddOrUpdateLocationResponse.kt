package com.am.projectinternalresto.data.response.super_admin.location

import com.google.gson.annotations.SerializedName

data class AddOrUpdateLocationResponse(

    @field:SerializedName("data")
    val data: DataItemLocation? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

