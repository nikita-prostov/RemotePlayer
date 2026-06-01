package com.nks.interactive.remoteplayer.controller

import android.app.Application
import com.nks.interactive.remoteplayer.controller.api.ApiClient
import com.nks.interactive.remoteplayer.controller.koin.appModule
import com.nks.interactive.remoteplayer.controller.koin.viewModelModule
import com.nks.interactive.remoteplayer.controller.localStorage.AppSettingsStorage
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    lateinit var settings: AppSettingsStorage

    override fun onCreate() {
        super.onCreate()
        settings = AppSettingsStorage(this)

        if (settings.ipAddress.isNotEmpty()) {
            ApiClient.init(settings.ipAddress)
        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, viewModelModule)
        }
    }
}