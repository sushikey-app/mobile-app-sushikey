package com.am.projectinternalresto.data.body_params

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class MenuBody(
    var idCategory: String,
    var nameMenu: String,
    var composition: String,
    var quota: Int,
    var price: Int,
    var image: File? = null,
    var itemToppings: List<ItemTopping>? = null
)

data class ItemTopping(
    val nama: String,
    val price: Int
)


fun MenuBody.toMultipartBody(): Map<String, RequestBody> {
    val map = mutableMapOf<String, RequestBody>()

    map["kategori_id"] = idCategory.toRequestBody("text/plain".toMediaTypeOrNull())
    map["nama"] = nameMenu.toRequestBody("text/plain".toMediaTypeOrNull())
    map["komposisi"] = composition.toRequestBody("text/plain".toMediaTypeOrNull())
    map["kuota"] = quota.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    map["harga"] = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())

    itemToppings?.forEachIndexed { index, topping ->
        map["topping[$index][nama]"] = topping.nama.toRequestBody("text/plain".toMediaTypeOrNull())
        map["topping[$index][harga]"] =
            topping.price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }
    return map
}

fun MenuBody.toMultipartImagePart(): MultipartBody.Part? {
    return image?.let {
        val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("image", it.name, requestBody)
    }
}
