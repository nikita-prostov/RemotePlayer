package com.nks.interactive.remoteplayer.controller.models

data class ServerState(
    val currentTrack: TrackInfo?,
    val isPlaying: Boolean,
    val volume:Float,
    val isShuffled: Boolean
)
