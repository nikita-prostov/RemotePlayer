package com.nks.interactive.remoteplayer.controller.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nks.interactive.remoteplayer.controller.api.ApiService
import com.nks.interactive.remoteplayer.controller.models.TrackInfo
import com.nks.interactive.remoteplayer.controller.models.ServerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.inc

class ControllerScreenVM(private val apiService: ApiService) : ViewModel() {

    private var _isInitialized = false
    private var _currentTrack by mutableStateOf<TrackInfo?>(null)
    private var _isPlaying = mutableStateOf(false)
    private var _isShuffled = mutableStateOf(false)
    private var _volume = mutableFloatStateOf(1f)
    private var _trackList =  mutableStateOf<List<TrackInfo>>(emptyList())
    private var _isLoading = mutableStateOf(true)
    private var lastPage = 1

    private var volumeJob: Job? = null

    val currentTrack: TrackInfo?
        get() = _currentTrack

    val isPlaying: Boolean
        get() = _isPlaying.value

    var volume: Float
        get() = _volume.value
        set(value){
            _volume.value = value;
            volumeJob?.cancel()
            volumeJob = viewModelScope.launch {
                delay(300)
                apiService.setVolume(value)
            }
        }

    val isShuffled: Boolean
        get() = _isShuffled.value
    val isLoading: Boolean
        get() = _isLoading.value
    val tracks:List<TrackInfo>
        get() = _trackList.value
    fun getCurrentState(){
        if(!_isInitialized){
            viewModelScope.launch {
                val response = apiService.getState()
                if (response.currentTrack != null) {
                    _currentTrack = response.currentTrack
                }
                _isPlaying.value = response.isPlaying
                _isShuffled.value = response.isShuffled
                _volume.value = response.volume
                _isInitialized = true
            }
        }
    }

    fun shuffle(){
        viewModelScope.launch {
            if(!_isShuffled.value)
                apiService.shuffle()
            else
                apiService.sort()
            _isShuffled.value = !isShuffled
            _isPlaying.value = false
        }
    }
    fun play() {
        if (!_isPlaying.value) {
            viewModelScope.launch {
                _currentTrack = apiService.play()?.body()
                _isPlaying.value = true
            }
        }
    }

    fun pause() {
        if (_isPlaying.value) {
            viewModelScope.launch {
                apiService.pause()
                _isPlaying.value = false
            }
        }
    }

    fun nextTrack(){
        viewModelScope.launch {
            _currentTrack = apiService.switch("next")
            _isPlaying.value = true
        }
    }

    fun prevTrack(){
        viewModelScope.launch {
            _currentTrack = apiService.switch("prev")
            _isPlaying.value = true
        }
    }
    fun getAll(page:Int){
        _isLoading.value = true
        viewModelScope.launch {
            _trackList.value = apiService.getAll(page=page)
            _isLoading.value = false
            lastPage = page
        }
    }

    fun loadMore(){
        lastPage++
        viewModelScope.launch {
            val tracks = apiService.load(page = lastPage).toMutableList()
            val oldTracks = _trackList.value.toMutableList()
            for (track in tracks){
                oldTracks.add(track)
            }
            _trackList.value = oldTracks
        }
    }

    fun play(trackInfo: TrackInfo){
        viewModelScope.launch {
            _currentTrack = apiService.play(trackInfo)
            _isPlaying.value = true
        }
    }
}