package com.am.projectinternalresto.service.di

import com.am.projectinternalresto.service.api.ApiConfig
import com.am.projectinternalresto.service.source.LocationRepository
import com.am.projectinternalresto.service.source.MenuRepository
import com.am.projectinternalresto.service.source.UserRepository
import com.am.projectinternalresto.ui.feature.admin.manage_category.ManageCategoryViewModel
import com.am.projectinternalresto.ui.feature.admin.manage_menu.ManageMenuViewModel
import com.am.projectinternalresto.ui.feature.admin.manage_staff.ManageStaffViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_admin.ManageAdminViewModel
import com.am.projectinternalresto.ui.feature.super_admin.manage_location.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModule {
    val apiModule = module {
        single { ApiConfig.getApiService() }
        single { UserRepository(get()) }
        single { LocationRepository(get()) }
        single { MenuRepository(get()) }
    }

    val uiModule
        get() = module {
            viewModel { AuthViewModel(get()) }
            viewModel { LocationViewModel(get()) }
            viewModel { ManageAdminViewModel(get()) }
            viewModel { ManageCategoryViewModel(get()) }
            viewModel { ManageMenuViewModel(get()) }
            viewModel { ManageStaffViewModel(get()) }
        }
}