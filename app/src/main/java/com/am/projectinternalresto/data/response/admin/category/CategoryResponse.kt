package com.am.projectinternalresto.data.response.admin.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CategoryResponse(

    @field:SerializedName("data")
    val data: List<DataItemCategory?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

@Parcelize
data class DataItemCategory(

    @field:SerializedName("nama")
    val nameCategory: String? = null,

    @field:SerializedName("kode")
    val codeCategory: String? = null,

    @field:SerializedName("id")
    val id: String
) : Parcelable
