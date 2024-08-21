package com.am.projectinternalresto.data.response.admin.category

import com.google.gson.annotations.SerializedName

data class AddOrUpdateCategoryResponse(

    @field:SerializedName("data")
    val data: DataItemCategory? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
