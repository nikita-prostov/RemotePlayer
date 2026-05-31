package com.nks.interactive.remoteplayer.controller.koin

import com.nks.interactive.remoteplayer.controller.api.ApiClient
import com.nks.interactive.remoteplayer.controller.api.ApiService
import com.nks.interactive.remoteplayer.controller.localStorage.AppSettingsStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { AppSettingsStorage(androidContext()) }
    single { ApiClient }
    single<ApiService> { ApiClient.api }
}