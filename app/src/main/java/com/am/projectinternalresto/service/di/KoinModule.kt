package com.am.projectinternalresto.service.di

import com.am.projectinternalresto.service.api.ApiConfig
import com.am.projectinternalresto.service.source.UserRepository
import org.koin.dsl.module

object KoinModule {
    val apiModule = module {
        single { ApiConfig.getApiService() }
        factory {UserRepository(get()) }
    }

    val uiModule = module {
        /*inject view model*/
    }
}