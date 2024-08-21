package com.am.projectinternalresto.data.response.admin.manage_staff

import com.google.gson.annotations.SerializedName

data class AddOrUpdateStaffResponse(

    @field:SerializedName("data")
    val data: DataItemStaff? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
