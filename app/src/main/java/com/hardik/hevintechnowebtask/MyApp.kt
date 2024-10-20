package com.hardik.hevintechnowebtask

import android.app.Application
import com.hardik.hevintechnowebtask.di.AppModule
import com.hardik.hevintechnowebtask.di.AppModuleImpl

class MyApp: Application() {
    companion object {
        lateinit var appModule : AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}