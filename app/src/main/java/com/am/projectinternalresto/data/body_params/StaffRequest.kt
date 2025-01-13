package com.am.projectinternalresto.data.body_params

import com.google.gson.annotations.SerializedName

data class StaffRequest(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("username") val username: String,
    @field:SerializedName("nomor_telephone") val phoneNumber: String,
    @field:SerializedName("password") val password: String,
)
