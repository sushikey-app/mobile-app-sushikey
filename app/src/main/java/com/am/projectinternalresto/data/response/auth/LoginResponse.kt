package com.am.projectinternalresto.data.response.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data") val data: DataUserLogin? = null,

    @field:SerializedName("token") val token: String? = null
)

data class DataUserLogin(
    @field:SerializedName("role") val role: String? = null,
)
