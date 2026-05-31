package com.nks.interactive.remoteplayer.controller.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.viewmodels.SettingsScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(){
    val viewModel = koinViewModel<SettingsScreenVM>()
    var input by remember { mutableStateOf(viewModel.apiUrl) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Settings", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(input,  {input = it; errorMessage = ""}, Modifier
            .fillMaxWidth()
            .padding(8.dp), isError = errorMessage.isNotEmpty(), label = {Text("Enter api url:")})
        if(errorMessage.isNotEmpty()) {
            OutlinedCard(Modifier.fillMaxWidth(), colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )) {
                Box(Modifier.padding(8.dp)) {
                    Text(errorMessage)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton({
            if(input.startsWith("http://") || input.startsWith("https://")){
                viewModel.apiUrl = input
                errorMessage = ""
                successMessage = "Api url success saved"
            }
            else
                errorMessage = "Invalid api url"
        }) {
            Text("Save")
        }
        Spacer(Modifier.weight(1f))
        if(successMessage.isNotEmpty())
            OutlinedCard(Modifier.fillMaxWidth()) {
                Box(Modifier.padding(8.dp)) {
                    Text(successMessage)
                }
            }
    }
}