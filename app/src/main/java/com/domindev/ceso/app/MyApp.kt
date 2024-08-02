package com.domindev.ceso.app

import android.app.Application
import com.domindev.ceso.di.AppModule
import com.domindev.ceso.di.AppModuleImpl

class MyApp: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}