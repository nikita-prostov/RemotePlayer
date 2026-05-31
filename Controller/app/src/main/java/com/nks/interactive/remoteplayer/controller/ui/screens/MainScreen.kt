package com.nks.interactive.remoteplayer.controller.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.ui.components.MenuButton
import com.nks.interactive.remoteplayer.controller.ui.theme.onPrimaryDark
import com.nks.interactive.remoteplayer.controller.ui.theme.onPrimaryLight

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableIntStateOf(0) }

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