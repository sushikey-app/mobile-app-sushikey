package com.am.projectinternalresto.data.params

import com.google.gson.annotations.SerializedName

data class AdminAndSuperAdminBody(
    @field:SerializedName("lokasi_id") val locationId: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("username") val username: String,
    @field:SerializedName("nomor_telephone") val phoneNumber: String,
    @field:SerializedName("password") val password: String,
    @field:SerializedName("role") val role: String,
)


