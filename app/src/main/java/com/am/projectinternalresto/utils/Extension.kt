package com.am.projectinternalresto.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

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