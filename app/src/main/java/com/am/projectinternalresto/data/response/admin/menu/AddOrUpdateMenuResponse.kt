package com.am.projectinternalresto.data.response.admin.menu

import com.google.gson.annotations.SerializedName

data class AddOrUpdateMenuResponse(

    @field:SerializedName("data")
    val data: DataItemAddOrUpdateMenu? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemAddOrUpdateMenu(

    @field:SerializedName("toppings")
    val toppings: List<Any?>? = null,

    @field:SerializedName("menu")
    val menu: DataItemMenu? = null
)
