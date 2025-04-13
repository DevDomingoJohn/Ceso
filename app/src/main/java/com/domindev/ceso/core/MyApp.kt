package com.domindev.ceso.core

import android.app.Application
import com.domindev.ceso.core.di.AppModule
import com.domindev.ceso.core.di.AppModuleImpl

class MyApp: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}