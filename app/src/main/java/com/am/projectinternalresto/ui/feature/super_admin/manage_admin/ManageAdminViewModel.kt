package com.am.projectinternalresto.ui.feature.super_admin.manage_admin

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.AdminAndSuperAdminRequest
import com.am.projectinternalresto.service.source.UserRepository

class ManageAdminViewModel(private val repository: UserRepository) : ViewModel() {
    fun getAllDataAdminAndSuperAdmin(token: String) = repository.getAllDataAdminSuperAdmin(token)
    fun searchAdminAndSuperAdmin(token: String, keyword : String) = repository.searchAdminAndSuperAdmin(token, keyword)
    fun addAdminOrSuperAdmin(token: String, payload: AdminAndSuperAdminRequest) =
        repository.addAdminOrSuperAdmin(token, payload)

    fun updateAdminOrSuperAdmin(
        token: String, idUser: String, payload: AdminAndSuperAdminRequest
    ) = repository.updateAdminOrSuperAdmin(token, idUser, payload)

    fun deleteAdminOrSuperAdmin(token: String, id: String) =
        repository.deleteAdminOrSuperAdmin(token, id)
}