package com.am.projectinternalresto.data.response.super_admin.location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LocationResponse(

    @field:SerializedName("data")
    val data: List<DataItemLocation?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

@Parcelize
data class DataItemLocation(

    @field:SerializedName("id")
    val id: String,


    @field:SerializedName("lokasi")
    val locationOutlet: String? = null,

    @field:SerializedName("nama_resto")
    val outletName: String? = null,

    @field:SerializedName("nomor_telephone")
    val phoneNumber: String? = null
) : Parcelable

data class Location(
    val id: String,
    val name: String
)