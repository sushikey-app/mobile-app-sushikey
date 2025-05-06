package com.am.projectinternalresto.ui.feature.admin.manage_menu

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
        return repository.updateMenu(token, idMenu, payload)
    }

    fun deleteMenu(token: String, idMenu: String) = repository.deleteMenu(token, idMenu)

    fun getMenuFavoriteSuperAdmin(
        token: String,
        locationId: String,
        startDate: Int? = null,
        startMonth: Int? = null,
        startYear: Int? = null,
        endDate: Int? = null,
        endMonth: Int? = null,
        endYear: Int? = null
    ) = repository.getMenuFavoriteSuperAdmin(
        token, locationId, startDate, startMonth, startYear, endDate, endMonth, endYear
    )

    fun getMenuFavoriteAdmin(token: String, categoryId: String) =
        repository.getMenuFavoriteAdmin(token, categoryId)

    fun searchMenu(token: String, keyword: String) = repository.searchMenu(token, keyword)
    fun filterMenuByCategory(token: String, idMenu: String) =
        repository.filterMenuByCategory(token, idMenu)
    fun filterMenuPesanByCategory(token: String, idMenu: String) =
        repository.filterMenuByCategory(token, idMenu)
}