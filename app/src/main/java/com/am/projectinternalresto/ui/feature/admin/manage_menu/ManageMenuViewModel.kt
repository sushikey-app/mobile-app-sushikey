package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.MenuBody
import com.am.projectinternalresto.data.response.admin.menu.AddOrUpdateMenuResponse
import com.am.projectinternalresto.service.source.MenuRepository
import com.am.projectinternalresto.service.source.Resource

class ManageMenuViewModel(private val repository: MenuRepository) : ViewModel() {
    fun getMenu(token: String) = repository.getAllDataMenu(token)
    fun addMenu(token: String, payload: MenuBody) = repository.addMenu(token, payload)

    fun updateMenu(
        token: String, idMenu: String, payload: MenuBody
    ): LiveData<Resource<AddOrUpdateMenuResponse?>> {

        Log.e("Check_viewmodel_manage_menu", "token : $token")
        Log.e("Check_viewmodel_manage_menu", "idMenu : $idMenu")

        return repository.updateMenu(token, idMenu, payload)
    }


    fun deleteMenu(token: String, idMenu: String) = repository.deleteMenu(token, idMenu)

    fun getMenuFavoriteSuperAdmin(token: String, locationId: String) =
        repository.getMenuFavoriteSuperAdmin(token, locationId)

    fun getMenuFavoriteAdmin(token: String, categoryId: String) =
        repository.getMenuFavoriteAdmin(token, categoryId)

    fun searchMenu(token: String, keyword: String) = repository.searchMenu(token, keyword)
}