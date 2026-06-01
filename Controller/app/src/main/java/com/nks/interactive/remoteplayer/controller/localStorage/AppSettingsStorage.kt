package com.nks.interactive.remoteplayer.controller.localStorage

import android.app.Activity
import android.content.Context
import androidx.core.content.edit

class AppSettingsStorage(context: Context) {
    private val prefs = context.getSharedPreferences("settings", Activity.MODE_PRIVATE)
    private var _ipAddress: String? = null
    private var _pollingFrequency: Int? = null

    var ipAddress: String
        get() {
            if (_ipAddress == null)
                _ipAddress = prefs.getString("ipAddress", "192.168.1.100") ?: "192.168.1.100"
            return _ipAddress!!
        }
        set(value) {
            prefs.edit { putString("ipAddress", value) }
            _ipAddress = value
        }

    var pollingFrequency: Int
        get() {
            if (_pollingFrequency == null)
                _pollingFrequency = prefs.getInt("pollingFrequency", 3)
            return _pollingFrequency!!
        }
        set(value) {
            prefs.edit { putInt("pollingFrequency", value) }
            _pollingFrequency = value
        }

    val pollingIntervalSeconds: Int
        get() = 60 / pollingFrequency
}