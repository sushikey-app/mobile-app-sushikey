package com.am.projectinternalresto.data.response.super_admin.manage_admin

import android.os.Parcelable
import com.am.projectinternalresto.data.response.super_admin.location.DataItemLocation
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ManageAdminAndSuperAdminResponse(

    @field:SerializedName("data")
    val data: List<DataItemManageAdminAndSuperAdmin?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

@Parcelize
data class DataItemManageAdminAndSuperAdmin(

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("lokasi_id")
    val locationId: String? = null,

    @field:SerializedName("nomor_telephone")
    val phoneNumber: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("lokasi")
    val location: DataItemLocation
) : Parcelable
