package com.am.projectinternalresto.data.response.super_admin.dashboard

import com.google.gson.annotations.SerializedName

data class MenuFavoriteResponse(

    @field:SerializedName("data")
    val data: List<DataItemMenuFavorite?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItemMenuFavorite(

    @field:SerializedName("nama")
    val nameMenu: String? = null,

    @field:SerializedName("total_penjualan")
    val totalSales: Int? = null
)
