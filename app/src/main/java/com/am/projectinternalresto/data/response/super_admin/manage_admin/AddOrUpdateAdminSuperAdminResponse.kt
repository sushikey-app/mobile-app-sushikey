package com.am.projectinternalresto.data.response.super_admin.manage_admin

import com.google.gson.annotations.SerializedName

data class AddOrUpdateAdminSuperAdminResponse(

	@field:SerializedName("data")
	val data: DataItemAddOrUpdateAdminSuperAdmin? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemAddOrUpdateAdminSuperAdmin(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lokasi_id")
	val lokasiId: String? = null,

	@field:SerializedName("nomor_telephone")
	val nomorTelephone: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
