package com.nks.interactive.remoteplayer.controller.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nks.interactive.remoteplayer.controller.ui.theme.ControllerTheme
import com.nks.interactive.remoteplayer.controller.viewmodels.ControllerScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreen(){
    val viewModel = koinViewModel<ControllerScreenVM>()
    viewModel.getAll(1)
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= viewModel.tracks.size - 5) {
                    viewModel.loadMore()
                }
            }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent,
        contentColor = Color.White
    ) {

        if(viewModel.isLoading){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize())
            {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading tracks...")
            }
        }
        else{
            LazyColumn(state = listState) {
                items(viewModel.tracks) { track ->
                    val isPlaying = track.artist == viewModel.currentTrack?.artist && track.title == viewModel.currentTrack?.title
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor =
                                if(isPlaying)MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                        ),
                        headlineContent = { Text(track.title) },
                        supportingContent = { Text(track.artist) },
                        modifier = Modifier.clickable { viewModel.play(track) }
                    )
                }
            }
        }
    }
}