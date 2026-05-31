package com.nks.interactive.remoteplayer.controller.localStorage

import android.app.Activity
import android.content.Context
import androidx.core.content.edit

class AppSettingsStorage(context: Context) {
    private val prefs = context.getSharedPreferences("settings",Activity.MODE_PRIVATE)
    private var _apiUrl:String? = null
    var apiUrl:String
        get() {
            if(_apiUrl == null)
                _apiUrl = prefs.getString("apiUrl","")
            return _apiUrl!!
        }
        set(value) {
            prefs.edit { putString("apiUrl", value) }
            _apiUrl = value
        }
}