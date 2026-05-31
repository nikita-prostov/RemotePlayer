package com.nks.interactive.remoteplayer.controller.viewmodels

import androidx.lifecycle.ViewModel
import com.nks.interactive.remoteplayer.controller.api.ApiClient
import com.nks.interactive.remoteplayer.controller.localStorage.AppSettingsStorage
import org.koin.java.KoinJavaComponent.inject

class SettingsScreenVM(
    private val settingsStorage: AppSettingsStorage,
    private val apiClient: ApiClient) : ViewModel() {

    var apiUrl:String
        get() = settingsStorage.apiUrl
        set(value) {
            settingsStorage.apiUrl = value
            apiClient.init(value)
        }
}