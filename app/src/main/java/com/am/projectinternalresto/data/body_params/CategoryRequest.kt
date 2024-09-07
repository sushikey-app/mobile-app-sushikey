package com.am.projectinternalresto.data.body_params

import com.google.gson.annotations.SerializedName

data class CategoryRequest(
    @field:SerializedName("kode") val codeCategory: String,
    @field:SerializedName("nama") val nameCategory: String,
)