package com.am.projectinternalresto.ui.feature

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.am.projectinternalresto.service.source.UserPreference
import com.am.projectinternalresto.ui.feature.auth.LoginActivity

abstract class BaseActivity : AppCompatActivity() {
    protected fun checkLoginStatus() {
        Log.e("CheckLOgin", "function check login status")
        val userPref = UserPreference.getInstance()
        if (userPref.isUserLogin()) {
            val loginTimestamp = userPref.getLoginTimestamp()
            val currentTime = System.currentTimeMillis()

            if (currentTime - loginTimestamp > UserPreference.ONE_DAY_MILLIS) {
                userPref.clearToken()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}