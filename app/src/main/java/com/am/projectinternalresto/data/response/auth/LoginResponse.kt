package com.am.projectinternalresto.data.response.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(

    @field:SerializedName("data") val data: DataUserLogin? = null,

    @field:SerializedName("token") val token: String? = null
)

data class DataUserLogin(
    @field:SerializedName("role") val role: String? = null,
    @field:SerializedName("username") val username: String? = null,
    @field:SerializedName("lokasi_id") val locationId: String? = null,

)
