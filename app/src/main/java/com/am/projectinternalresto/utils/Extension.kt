package com.am.projectinternalresto.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Activity.goToActivity(
    targetActivity: Class<out Activity>,
    withFinish: Boolean = false,
    bundle: Bundle? = null
) {
    val intent = Intent(this, targetActivity)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
    if (withFinish) finish()
}
//
//fun Date.toISO8601String(): String {
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    return dateFormat.format(this)
//}

fun String.toPlainTextRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())

fun File.toMultipartBody(partName: String): MultipartBody.Part =
    MultipartBody.Part.createFormData(
        partName, name, asRequestBody("image/jpeg".toMediaTypeOrNull())
    )
