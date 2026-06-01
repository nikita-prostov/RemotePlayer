package com.nks.interactive.remoteplayer.controller.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nks.interactive.remoteplayer.controller.api.ApiService
import com.nks.interactive.remoteplayer.controller.localStorage.AppSettingsStorage
import com.nks.interactive.remoteplayer.controller.models.TrackInfo
import com.nks.interactive.remoteplayer.controller.models.ServerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.inc

class ControllerScreenVM(private val apiService: ApiService, private val settingsStorage: AppSettingsStorage) : ViewModel() {

    private var _isInitialized = false
    private var _currentTrack by mutableStateOf<TrackInfo?>(null)
    private var _nextTrack by mutableStateOf<TrackInfo?>(null)
    private var _isPlaying = mutableStateOf(false)
    private var _isShuffled = mutableStateOf(false)
    private var _volume = mutableFloatStateOf(1f)
    private var _trackList =  mutableStateOf<List<TrackInfo>>(emptyList())
    private var _isLoading = mutableStateOf(true)
    private var lastPage = 1

    private var volumeJob: Job? = null
    private var _pollingJob: Job? = null

    val currentTrack: TrackInfo?
        get() = _currentTrack

    val nextTrack: TrackInfo?
        get() = _nextTrack

    val isPlaying: Boolean
        get() = _isPlaying.value

    var volume: Float
        get() = _volume.floatValue
        set(value){
            _volume.floatValue = value;
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

    fun startPolling() {
        _pollingJob?.cancel()
        val interval = settingsStorage.pollingIntervalSeconds * 1000L
        _pollingJob = viewModelScope.launch {
            while (isActive) {
                refreshState()
                delay(interval)
            }
        }
    }

    fun stopPolling(){
        _pollingJob?.cancel()
    }

    private suspend fun refreshState() {
        try {
            val currentResponse = apiService.getCurrent()
            if (currentResponse.isSuccessful) {
                _currentTrack = currentResponse.body()
            }

            val nextResponse = apiService.getNext()
            if (nextResponse.isSuccessful) {
                _nextTrack = nextResponse.body()
            }
        } catch (e: Exception) {
            Log.e("ControllerVM", "Polling failed: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        _pollingJob?.cancel()
    }

    fun getCurrentState(){
        if(!_isInitialized){
            viewModelScope.launch {
                val response = apiService.getState()
                if (response.currentTrack != null) {
                    _currentTrack = response.currentTrack
                }
                _isPlaying.value = response.isPlaying
                _isShuffled.value = response.isShuffled
                _volume.floatValue = response.volume

                val nextTrackResponse = apiService.getNext()
                _nextTrack = nextTrackResponse.body()
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
                _nextTrack = apiService.getNext().body()
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
            _nextTrack = apiService.getNext().body()
            _isPlaying.value = true
        }
    }

    fun prevTrack(){
        viewModelScope.launch {
            _currentTrack = apiService.switch("prev")
            _nextTrack = apiService.getNext().body()
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
            _nextTrack = apiService.getNext().body()
            _isPlaying.value = true
        }
    }
}