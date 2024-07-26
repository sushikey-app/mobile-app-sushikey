package com.am.projectinternalresto.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.am.projectinternalresto.R
import com.am.projectinternalresto.ui.feature.auth.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.textfield.TextInputLayout
import io.github.muddz.styleabletoast.StyleableToast

object UiHandler {
    fun setHintBehavior(vararg editLayout: TextInputLayout) {
        for (edl in editLayout) {
            edl.isHintEnabled = false
        }
    }

    fun toastSuccessMessage(context: Context, message: String) {
        StyleableToast
            .Builder(context)
            .text(message)
            .textColor(Color.WHITE)
            .backgroundColor(Color.GREEN)
            .cornerRadius(16)
            .font(R.font.pop_semi_bold)
            .textSize(18F)
            .show()
    }

    fun toastErrorMessage(context: Context, message: String) {
        StyleableToast
            .Builder(context)
            .text(message)
            .textColor(Color.WHITE)
            .backgroundColor(Color.RED)
            .cornerRadius(16)
            .font(R.font.pop_semi_bold)
            .textSize(18F)
            .show()
    }
}