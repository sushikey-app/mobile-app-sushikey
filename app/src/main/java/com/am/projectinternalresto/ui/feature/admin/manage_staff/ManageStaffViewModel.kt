package com.am.projectinternalresto.ui.feature.admin.manage_staff

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.StaffRequest
import com.am.projectinternalresto.service.source.UserRepository

class ManageStaffViewModel(private val repository: UserRepository) : ViewModel() {
    fun getAllDataStaff(token: String) = repository.getAllDataStaff(token)
    fun addStaff(token: String, dataStaff: StaffRequest) = repository.addStaff(token, dataStaff)
    fun updateStaff(token: String, idStaff: String, dataStaff: StaffRequest) =
        repository.updateStaff(token, idStaff, dataStaff)

    fun deleteStaff(token: String, idStaff: String) = repository.deleteStaff(token, idStaff)
    fun searchStaff(token: String, keyword: String) = repository.searchStaff(token, keyword)
}