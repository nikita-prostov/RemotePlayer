package com.nks.interactive.remoteplayer.controller.loggers

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class HttpRequestLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.i("HttpRequestLogger", message)
    }
}