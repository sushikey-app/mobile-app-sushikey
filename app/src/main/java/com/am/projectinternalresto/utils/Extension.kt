package com.am.projectinternalresto.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.math.absoluteValue

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

fun EditText.setPriceWatcherUtils(
    onPriceChanged: (Int) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        private var currentText = ""

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (text.toString() != currentText) {
                removeTextChangedListener(this)

                val cleanString = text.toString().replace("[Rp,.]".toRegex(), "").trim()
                if (cleanString.isNotEmpty()) {
                    val price = cleanString.toInt()
                    val formattedPrice = Formatter.formatCurrency(price)

                    currentText = formattedPrice
                    setText(formattedPrice)
                    setSelection(formattedPrice.length)
                    onPriceChanged(price)
                } else {
                    onPriceChanged(0)
                }

                addTextChangedListener(this)
            }
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    addTextChangedListener(textWatcher)
    return textWatcher
}

fun EditText.setMinusPrice(
    onPriceChanged: (Int) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        private var currentText = ""

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (text.toString() != currentText) {
                removeTextChangedListener(this)

                val cleanString = text.toString().replace("[Rp,.]".toRegex(), "").trim()
                if (cleanString.isNotEmpty()) {
                    try {
                        // Handle negative sign
                        val isNegative = cleanString.startsWith("-")
                        val numberOnly = cleanString.replace("-", "")

                        if (numberOnly.isNotEmpty()) {
                            val price = numberOnly.toInt() * (if (isNegative) -1 else 1)
                            val formattedPrice = if (isNegative) {
                                "-${Formatter.formatCurrency(price.absoluteValue)}"
                            } else {
                                Formatter.formatCurrency(price)
                            }

                            currentText = formattedPrice
                            setText(formattedPrice)
                            setSelection(formattedPrice.length)
                            onPriceChanged(price)
                        } else {
                            onPriceChanged(0)
                        }
                    } catch (e: NumberFormatException) {
                        onPriceChanged(0)
                    }
                } else {
                    onPriceChanged(0)
                }

                addTextChangedListener(this)
            }
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    addTextChangedListener(textWatcher)
    return textWatcher
}