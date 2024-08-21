package com.am.projectinternalresto.data.params

import com.google.gson.annotations.SerializedName

data class CategoryBody(
    @field:SerializedName("kode") val codeCategory: String,
    @field:SerializedName("nama") val nameCategory: String,
)