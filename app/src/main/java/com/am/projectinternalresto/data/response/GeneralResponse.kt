package com.am.projectinternalresto.data.response

import com.google.gson.annotations.SerializedName

data class GeneralResponse(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
