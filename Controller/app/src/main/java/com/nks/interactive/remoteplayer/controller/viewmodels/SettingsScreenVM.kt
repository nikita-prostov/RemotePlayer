package com.nks.interactive.remoteplayer.controller.viewmodels

import androidx.lifecycle.ViewModel
import com.nks.interactive.remoteplayer.controller.api.ApiClient
import com.nks.interactive.remoteplayer.controller.localStorage.AppSettingsStorage
import org.koin.java.KoinJavaComponent.inject

class SettingsScreenVM(
    private val settingsStorage: AppSettingsStorage,
    private val apiClient: ApiClient
) : ViewModel() {

    var ipAddress: String
        get() = settingsStorage.ipAddress
        set(value) {
            settingsStorage.ipAddress = value
            apiClient.init("http://$value:5000/")
        }

    var pollingFrequency: Int
        get() = settingsStorage.pollingFrequency
        set(value) {
            settingsStorage.pollingFrequency = value.coerceIn(1, 60)
        }

    val pollingIntervalDescription: String
        get() = when {
            pollingFrequency >= 60 -> "каждую секунду"
            pollingFrequency >= 30 -> "каждые 2 секунды"
            pollingFrequency >= 20 -> "каждые 3 секунды"
            pollingFrequency >= 10 -> "каждые ${60 / pollingFrequency} секунд"
            pollingFrequency >= 6 -> "каждые 10 секунд"
            pollingFrequency >= 2 -> "каждые ${60 / pollingFrequency} секунд"
            else -> "каждую минуту"
        }
}