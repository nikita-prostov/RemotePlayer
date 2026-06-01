package com.nks.interactive.remoteplayer.controller.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nks.interactive.remoteplayer.controller.api.ApiClient
import com.nks.interactive.remoteplayer.controller.api.ApiService
import com.nks.interactive.remoteplayer.controller.localStorage.AppSettingsStorage
import kotlinx.coroutines.launch

class MainScreenVM(private val apiClient: ApiClient, private val settingsStorage: AppSettingsStorage) : ViewModel() {
    var serverAvailable by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun checkServer() {
        viewModelScope.launch {
            try {
                val ip = settingsStorage.ipAddress
                val initResult = apiClient.init(ip)

                if (initResult.isFailure) {
                    serverAvailable = false
                    errorMessage = "Неверный IP-адрес: $ip"
                    return@launch
                }

                // Проверяем, что сервер отвечает
                val response = apiClient.api.ping() // Нужен эндпоинт /ping
                serverAvailable = response.isSuccessful
                errorMessage = if (!serverAvailable) "Сервер не отвечает" else null
            } catch (e: Exception) {
                serverAvailable = false
                errorMessage = "Ошибка подключения: ${e.message}"
            }
        }
    }
}