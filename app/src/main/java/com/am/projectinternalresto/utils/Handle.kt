package com.am.projectinternalresto.utils

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.am.projectinternalresto.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object UiHandle {
    fun setupDisableHintForField(vararg editLayout: TextInputLayout) {
        for (edl in editLayout) {
            edl.isHintEnabled = false
        }
    }

    fun setupClearTextForField(vararg editText: TextInputEditText) {
        for (edt in editText) {
            edt.setText("")
        }
    }

    fun setupDisplayDataFromSearchOrGet(
        editLayout: TextInputLayout,
        editText: TextInputEditText,
        onSearchDisplayData: ((String) -> Unit)? = null,
        onDisplayDataDefault: (() -> Unit)? = null,
    ) {
        setupDisableHintForField(editLayout)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrEmpty() || text.length < 2) {
                    onDisplayDataDefault?.invoke()
                } else {
                    onSearchDisplayData?.invoke(text.toString())
                }
            }
        })
    }

    fun setupHideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

object ProgressHandle {
    fun setupVisibilityProgressBar(
        progressBar: ProgressBar, textLoading: TextView, isVisible: Boolean
    ) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        textLoading.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setupVisibilityShimmerLoadingInLinearLayout(layout: LinearLayout, isVisible: Boolean) {
        layout.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setupVisibilityShimmerLoadingLayout(layout: ShimmerFrameLayout, isVisible: Boolean) {
        layout.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

object NotificationHandle {
    fun showSuccessSnackBar(view: View, message: String) {
        createSnackBar(view, message, R.layout.layout_alert_success)
    }

    fun showErrorSnackBar(view: View, message: String) {
        createSnackBar(view, message, R.layout.layout_alert_error)
    }

    private fun createSnackBar(view: View, message: String, layoutId: Int) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(view.context).inflate(layoutId, null)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        // Hapus background default Snackbar
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

        // Hapus padding default
        snackbarLayout.setPadding(0, 0, 0, 0)

        val messageView = customView.findViewById<TextView>(R.id.messageView)
        messageView.text = message

        snackbarLayout.removeAllViews()
        snackbarLayout.addView(customView, 0)  // Tambahkan customView sebagai child pertama

        // Atur layout params untuk memastikan Snackbar menggunakan lebar penuh
        val params = customView.layoutParams as FrameLayout.LayoutParams
        ViewGroup.LayoutParams.MATCH_PARENT.also { params.width = it }
        params.gravity = Gravity.BOTTOM
        customView.layoutParams = params

        snackbar.show()
    }
}
