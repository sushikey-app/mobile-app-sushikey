package com.am.projectinternalresto.data.params

import com.google.gson.annotations.SerializedName

data class LocationBody(
    @field:SerializedName("nama_resto") val nameOutlet: String,
    @field:SerializedName("lokasi") val locationOutlet: String,
    @field:SerializedName("nomor_telephone") val phoneNumber: String,
)


