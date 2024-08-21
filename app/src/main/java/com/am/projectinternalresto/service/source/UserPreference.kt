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
            apply()
        }
    }

    fun getTokenUser(): String? {
        return sharePref.getString(KEY_TOKEN, null)
    }

    fun isUserLogin(): Boolean {
        return sharePref.getBoolean(KEY_LOGIN, false)
    }

    fun clearToken() {
        sharePref.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_TOKEN = "token"
        private const val KEY_LOGIN = "isLogin"

        @Volatile
        private var instance: UserPreference? = null

        fun getInstance(): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference().also { instance = it }
            }
        }
    }
}