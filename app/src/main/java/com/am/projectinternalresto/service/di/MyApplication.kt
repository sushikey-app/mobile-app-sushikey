package com.am.projectinternalresto.service.di

import android.app.Application
import com.am.projectinternalresto.service.di.KoinModule.apiModule
import com.am.projectinternalresto.service.di.KoinModule.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    apiModule,
                    uiModule
                )
            )
        }
    }
}