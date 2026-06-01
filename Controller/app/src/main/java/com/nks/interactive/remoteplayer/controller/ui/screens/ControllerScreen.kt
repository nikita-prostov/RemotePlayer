package com.nks.interactive.remoteplayer.controller.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.R
import com.nks.interactive.remoteplayer.controller.ui.theme.ControllerTheme
import com.nks.interactive.remoteplayer.controller.ui.theme.onPrimaryLight
import com.nks.interactive.remoteplayer.controller.ui.theme.primaryDark
import com.nks.interactive.remoteplayer.controller.ui.theme.primaryLight
import com.nks.interactive.remoteplayer.controller.ui.theme.secondaryLight
import com.nks.interactive.remoteplayer.controller.viewmodels.ControllerScreenVM
import org.koin.androidx.compose.koinViewModel


@Composable
fun ControllerScreen(){
    val viewModel = koinViewModel<ControllerScreenVM>()
    LaunchedEffect(Unit) {
        viewModel.startPolling()
        viewModel.getCurrentState()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopPolling()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onBackground)) {
            Column(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Center) {
                Text("Сейчас играет:")
                Spacer(Modifier.height(8.dp))
                Text("Трек: "+(viewModel.currentTrack?.title?: "Нет трека"))
                Spacer(Modifier.height(8.dp))
                Text("Исполнитель: "+(viewModel.currentTrack?.artist?: "Неизвестный исполнитель"))
            }
        }
        Spacer(Modifier.height(24.dp))
        OutlinedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground)) {
            Column(Modifier.padding(8.dp).fillMaxWidth()) {
                Text("Далее:", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(4.dp))
                Text("Трек: "+(viewModel.nextTrack?.title?: "Нет трека"),
                        style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(4.dp))
                Text("Исполнитель: "+(viewModel.nextTrack?.artist?: "Неизвестный исполнитель"),
                    style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {

            OutlinedButton({
                viewModel.prevTrack()
            }) {
                Icon(painterResource(R.drawable.fast_rewind),"Prev")
            }
            Spacer(Modifier.width(16.dp))
            OutlinedButton({
                if(viewModel.isPlaying) viewModel.pause()
                else viewModel.play()
            }) {
                Icon(
                    if(viewModel.isPlaying) painterResource(R.drawable.pause) else painterResource(R.drawable.play),
                    contentDescription = "Play/Pause")
            }
            Spacer(Modifier.width(16.dp))
            OutlinedButton({
                viewModel.nextTrack()
            }) {
                Icon(painterResource(R.drawable.fast_forward),"Next")
            }
            Spacer(Modifier.width(16.dp))
            OutlinedButton({
                viewModel.shuffle()
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(viewModel.isShuffled) MaterialTheme.colorScheme.onPrimaryContainer else Color.Transparent,
                    contentColor = if(viewModel.isShuffled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                ),
                border = BorderStroke(1.dp, if(viewModel.isShuffled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.outline)
            )
            {
                Icon(
                    painterResource(R.drawable.shuffle),
                    "Shuffle",
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(
                painterResource(R.drawable.volume_down),
                contentDescription = "Тише",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Slider(
                value = viewModel.volume,
                onValueChange = { viewModel.volume = it },
                valueRange = 0f..1f,
                steps = 20,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )

            Icon(
                painterResource(R.drawable.volume_up),
                contentDescription = "Громче",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}