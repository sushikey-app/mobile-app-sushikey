package com.am.projectinternalresto.ui.feature.admin.manage_staff

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.params.StaffBody
import com.am.projectinternalresto.service.source.UserRepository

class ManageStaffViewModel(private val repository: UserRepository) : ViewModel() {
    fun getAllDataStaff(token: String) = repository.getAllDataStaff(token)
    fun addStaff(token: String, dataStaff: StaffBody) = repository.addStaff(token, dataStaff)
    fun updateStaff(token: String, idStaff: String, dataStaff: StaffBody) =
        repository.updateStaff(token, idStaff, dataStaff)

    fun deleteStaff(token: String, idStaff: String) = repository.deleteStaff(token, idStaff)
}