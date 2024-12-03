package com.am.projectinternalresto.ui.feature.admin.manage_category

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.body_params.CategoryRequest
import com.am.projectinternalresto.service.source.MenuRepository

class ManageCategoryViewModel(private val repository: MenuRepository) : ViewModel() {
    fun getCategoryMenu(token: String) = repository.getCategoryMenu(token)
    fun addCategoryMenu(token: String, categoryRequest: CategoryRequest) =
        repository.addCategoryMenu(token, categoryRequest)

    fun searchCategoryMenu(token: String, keyword: String) =
        repository.searchCategoryMenu(token, keyword)

    fun updateCategoryMenu(
        token: String,
        idCategory: String,
        payload: CategoryRequest
    ) = repository.updateCategoryMenu(token, idCategory, payload)

    fun deleteCategoryMenu(token: String, idCategory: String) =
        repository.deleteCategoryMenu(token, idCategory)
}