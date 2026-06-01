package com.nks.interactive.remoteplayer.controller.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.viewmodels.SettingsScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(onClose: (() -> Unit)? = null) {
    val viewModel = koinViewModel<SettingsScreenVM>()
    var ipInput by remember { mutableStateOf(viewModel.ipAddress) }
    var frequencyInput by remember { mutableStateOf(viewModel.pollingFrequency.toString()) }
    var ipError by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = ipInput,
            onValueChange = { ipInput = it; ipError = false },
            label = { Text("IP-адрес сервера") },
            placeholder = { Text("192.168.1.100") },
            isError = ipError,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )
        Text("Пример: 192.168.1.100", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = frequencyInput,
            onValueChange = {
                frequencyInput = it.filter { c -> c.isDigit() }
            },
            label = { Text("Частота опроса (раз в минуту)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )
        Text(
            "Сейчас: ${viewModel.pollingIntervalDescription}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        if (ipError) {
            Spacer(Modifier.height(8.dp))
            OutlinedCard(
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Неверный IP-адрес", modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(Modifier.height(8.dp))

        if (successMessage.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            OutlinedCard {
                Text(successMessage, modifier = Modifier.padding(8.dp))
            }
        }
        Spacer(Modifier.height(16.dp))
        Row{
            if(onClose != null){
                OutlinedButton(onClick = {
                    onClose.invoke()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Закрыть")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = {
                    if (ipInput.matches(Regex("^(\\d{1,3}\\.){3}\\d{1,3}$"))) {
                        viewModel.ipAddress = ipInput
                        val freq = frequencyInput.toIntOrNull() ?: 3
                        viewModel.pollingFrequency = freq
                        frequencyInput = viewModel.pollingFrequency.toString()
                        ipError = false
                        successMessage = "Настройки сохранены"
                    } else {
                        ipError = true
                    }
                }
            ) {
                Text("Сохранить")
            }

        }
    }
}