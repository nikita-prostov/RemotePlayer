package com.nks.interactive.remoteplayer.controller.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.ui.theme.primaryLight

@Composable
fun MenuButton(currentScreen: Int, activeOn:Int, text:String, icon: ImageVector, onClick: () -> Unit = {}){
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = if(currentScreen == activeOn) ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,      // цвет фона
            contentColor = MaterialTheme.colorScheme.secondary       // цвет текста
        ) else ButtonDefaults.buttonColors()
    ) {
        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = icon,
                contentDescription = text
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text)
        }
    }
}