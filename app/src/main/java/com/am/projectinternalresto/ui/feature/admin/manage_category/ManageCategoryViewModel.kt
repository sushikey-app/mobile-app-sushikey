package com.am.projectinternalresto.ui.feature.admin.manage_category

import androidx.lifecycle.ViewModel
import com.am.projectinternalresto.data.params.CategoryBody
import com.am.projectinternalresto.service.source.MenuRepository

class ManageCategoryViewModel(private val repository: MenuRepository) : ViewModel() {
    fun getCategoryMenu(token: String) = repository.getCategoryMenu(token)
    fun addCategoryMenu(token: String, categoryBody: CategoryBody) =
        repository.addCategoryMenu(token, categoryBody)

    fun updateCategoryMenu(
        token: String,
        idCategory: String,
        payload: CategoryBody
    ) = repository.updateCategoryMenu(token, idCategory, payload)

    fun deleteCategoryMenu(token: String, idCategory: String) =
        repository.deleteCategoryMenu(token, idCategory)
}