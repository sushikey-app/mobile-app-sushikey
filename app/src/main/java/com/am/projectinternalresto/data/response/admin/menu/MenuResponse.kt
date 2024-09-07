package com.am.projectinternalresto.data.response.admin.menu

import android.os.Parcelable
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class MenuResponse(

    @field:SerializedName("data")
    val data: List<DataItemMenu?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

@Parcelize
data class DataItemMenu(

    @field:SerializedName("komposisi")
    val composition: String? = null,

    @field:SerializedName("image")
    val imageMenu: String? = null,

    @field:SerializedName("kuota")
    val quota: Int? = null,

    @field:SerializedName("kategori")
    val category: DataItemCategory? = null,

    @field:SerializedName("nomor_menu")
    val noMenu: String? = null,

    @field:SerializedName("nama")
    val nameMenu: String? = null,

    @field:SerializedName("harga")
    val price: Int? = null,

    @field:SerializedName("id")
    val idMenu: String? = null,

    @field:SerializedName("topping")
    val topping: @RawValue List<ToppingItem>? = null
) : Parcelable


data class ToppingItem(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    var isSelected: Boolean = false
)
