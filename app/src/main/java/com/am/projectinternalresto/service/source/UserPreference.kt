package com.am.projectinternalresto.service.source

import android.content.Context
import android.content.SharedPreferences

class UserPreference private constructor() {
    private lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        sharePref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveTokenUser(token: String) {
        sharePref.edit().apply {
            putString(KEY_TOKEN, token)
            putBoolean(KEY_LOGIN, true)
            putLong(KEY_LOGIN_TIMESTAMP, System.currentTimeMillis())
            apply()
        }
    }

    fun getTokenUser(): String? {
        return sharePref.getString(KEY_TOKEN, null)
    }

    fun isUserLogin(): Boolean {
        return sharePref.getBoolean(KEY_LOGIN, false)
    }

    fun getLoginTimestamp(): Long {
        return sharePref.getLong(KEY_LOGIN_TIMESTAMP, 0)
    }

    fun clearToken() {
        sharePref.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_TOKEN = "token"
        private const val KEY_LOGIN = "isLogin"
        private const val KEY_LOGIN_TIMESTAMP = "login_timestamp"
        const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000


        @Volatile
        private var instance: UserPreference? = null

        fun getInstance(): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference().also { instance = it }
            }
        }
    }
}