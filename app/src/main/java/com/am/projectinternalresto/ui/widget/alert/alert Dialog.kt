package com.am.projectinternalresto.ui.widget.alert

import android.content.Context
import android.content.Intent
import com.am.projectinternalresto.R
import com.am.projectinternalresto.ui.feature.auth.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertDialog {

    // dialog for log out user
    fun showAlertLogout(context: Context) {
        val alertDialog = MaterialAlertDialogBuilder(context)
        alertDialog.apply {
            setTitle(R.string.title_nav_logout)
            setMessage("apakah anda yakin keluar?")
            setPositiveButton("Ya") { _, _ ->
                val targetActivity = Intent(context.applicationContext, LoginActivity::class.java)
                context.startActivity(targetActivity)
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }
}