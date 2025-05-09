package com.am.projectinternalresto.service.source

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

class UserPreference private constructor() {
    private lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        sharePref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveTokenUser(token: String, role: String) {
        val currentTime = System.currentTimeMillis() // Waktu sekarang dalam milidetik
        sharePref.edit().apply {
            putString(KEY_TOKEN, token) // Simpan token
            putString(KEY_ROLE, role)  // Simpan role
            putBoolean(KEY_LOGIN, true) // Tandai bahwa pengguna login
            putLong(KEY_LOGIN_TIME, currentTime) // Simpan waktu login
            apply()
        }
    }

    fun saveUserRole(role: String) {
        sharePref.edit().apply {
            putString(KEY_ROLE, role)
            apply()
        }
    }

    fun getTokenUser(): String? {
        return sharePref.getString(KEY_TOKEN, null)
    }

    fun getUserRole(): String? {
        return sharePref.getString(KEY_ROLE, null)
    }


    fun isUserLogin(): Boolean {
        return sharePref.getBoolean(KEY_LOGIN, false)
    }

    fun isLoginExpired(): Boolean {
        val loginTime = sharePref.getLong(KEY_LOGIN_TIME, 0)
        if (loginTime == 0L) return true // Jika belum ada waktu login, dianggap expired

        val currentCalendar = Calendar.getInstance() // Waktu saat ini
        val loginCalendar = Calendar.getInstance().apply {
            timeInMillis = loginTime // Waktu login
        }

        // Bandingkan apakah tanggal hari ini sudah berbeda dari tanggal login
        return currentCalendar.get(Calendar.YEAR) != loginCalendar.get(Calendar.YEAR) ||
                currentCalendar.get(Calendar.DAY_OF_YEAR) != loginCalendar.get(Calendar.DAY_OF_YEAR)
    }


    fun clearToken() {
        sharePref.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_TOKEN = "token"
        private const val KEY_LOGIN = "isLogin"
        private const val KEY_ROLE = "role"
        private const val KEY_LOGIN_TIME = "login_time"

        @Volatile
        private var instance: UserPreference? = null

        fun getInstance(): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference().also { instance = it }
            }
        }
    }
}