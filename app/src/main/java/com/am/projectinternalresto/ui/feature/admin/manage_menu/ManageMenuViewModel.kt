package com.am.projectinternalresto.ui.feature.admin.manage_menu

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.params.MenuBody
import com.am.projectinternalresto.service.source.MenuRepository

class ManageMenuViewModel(private val repository: MenuRepository) : ViewModel() {
    fun getMenu(token: String) = repository.getAllDataMenu(token)
    fun addMenu(token: String, payload: MenuBody) =
        repository.addMenu(token, payload)

    fun updateMenu(token: String, idMenu: String, payload: MenuBody) =
        repository.updateMenu(token, idMenu, payload)
}