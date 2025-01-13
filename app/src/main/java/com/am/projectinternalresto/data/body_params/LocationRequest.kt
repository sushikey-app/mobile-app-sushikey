package com.am.projectinternalresto.data.body_params

import com.google.gson.annotations.SerializedName

data class LocationRequest(
    @field:SerializedName("nama_resto") val nameOutlet: String,
    @field:SerializedName("lokasi") val locationOutlet: String,
    @field:SerializedName("nomor_telephone") val phoneNumber: String,
)


