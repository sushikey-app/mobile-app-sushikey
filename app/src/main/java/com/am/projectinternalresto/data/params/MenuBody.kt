package com.am.projectinternalresto.data.params

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class MenuBody(
    val idCategory: String,
    val noMenu: String,
    val nameMenu: String,
    val composition: String,
    val quota: Int,
    val price: Int,
    val image: File? = null
)

fun MenuBody.toMultipartBody(): Map<String, RequestBody> {
    val map = mutableMapOf<String, RequestBody>()

    map["kategori_id"] = idCategory.toRequestBody("text/plain".toMediaTypeOrNull())
    map["nomor_menu"] = noMenu.toRequestBody("text/plain".toMediaTypeOrNull())
    map["nama"] = nameMenu.toRequestBody("text/plain".toMediaTypeOrNull())
    map["komposisi"] = composition.toRequestBody("text/plain".toMediaTypeOrNull())
    map["kuota"] = quota.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    map["harga"] = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())

    return map
}

fun MenuBody.toMultipartImagePart(): MultipartBody.Part? {
    return image?.let {
        val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("image", it.name, requestBody)
    }
}
