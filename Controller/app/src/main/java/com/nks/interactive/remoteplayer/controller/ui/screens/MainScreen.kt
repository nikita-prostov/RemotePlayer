package com.nks.interactive.remoteplayer.controller.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.ui.components.MenuButton
import com.nks.interactive.remoteplayer.controller.ui.theme.onPrimaryDark
import com.nks.interactive.remoteplayer.controller.ui.theme.onPrimaryLight
import com.nks.interactive.remoteplayer.controller.viewmodels.MainScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainScreenVM>()
    var currentScreen by remember { mutableIntStateOf(0) }
    var openSettingsScreen by remember { mutableStateOf(false) }
    var openWarningScreen by remember { mutableStateOf(true) }
    LaunchedEffect(openSettingsScreen) {
        if (!openSettingsScreen) {
            viewModel.checkServer()
        }
    }

    if(openSettingsScreen){
        Row(modifier = Modifier.fillMaxSize().padding(16.dp,32.dp,64.dp,8.dp)) {
            SettingsScreen {
                openSettingsScreen = false
            }
        }
        openWarningScreen = false
    }
    
   if(viewModel.serverAvailable && !openSettingsScreen){
        Row(modifier = Modifier.fillMaxSize().padding(16.dp,32.dp,64.dp,8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                Spacer(Modifier.height(8.dp))
                MenuButton(currentScreen,0,"Main",Icons.Outlined.Home) { currentScreen = 0 }
                Spacer(Modifier.height(8.dp))
                MenuButton(currentScreen,1,"List", Icons.AutoMirrored.Outlined.List) { currentScreen = 1 }
                Spacer(Modifier.height(8.dp))
                MenuButton(currentScreen,2,"Options",Icons.Outlined.Settings) { currentScreen = 2 }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                when (currentScreen) {
                    0 -> ControllerScreen()
                    1 -> ListScreen()
                    2 -> SettingsScreen()
                }
            }

        }
    }
    else if(openWarningScreen){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(16.dp))
            Text(
                viewModel.errorMessage ?: "Сервер недоступен",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                openSettingsScreen = true
            }) {
                Text("Настроить подключение")
            }
            Button(onClick = { viewModel.checkServer() }) {
                Text("Повторить")
            }
        }
    }
}