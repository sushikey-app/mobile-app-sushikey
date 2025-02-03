package com.am.projectinternalresto.ui.feature.auth

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.service.source.UserPreference
import com.am.projectinternalresto.service.source.UserRepository

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val userPref = UserPreference.getInstance()

    fun login(username: String, password: String) = userRepository.login(username, password)

    fun saveTokenUser(token: String, role: String) = userPref.saveTokenUser(token, role)
    fun saveUserRole(role : String) = userPref.saveUserRole(role)
    fun getTokenUser(): String? = userPref.getTokenUser()
    fun getUserRole () : String? = userPref.getUserRole()
    fun isLoginExpired () : Boolean =userPref.isLoginExpired()
    fun isLoginUser() = userPref.isUserLogin()
    fun clearToken() = userPref.clearToken()

}