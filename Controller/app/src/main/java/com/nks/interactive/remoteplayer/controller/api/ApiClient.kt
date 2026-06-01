package com.nks.interactive.remoteplayer.controller.api

import com.nks.interactive.remoteplayer.controller.loggers.HttpRequestLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private var baseUrl: String = ""
    private var retrofit: Retrofit? = null

    private val logger = HttpRequestLogger()
    private val loggingInterceptor = HttpLoggingInterceptor(logger)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()


    val api: ApiService
        get() {
            val currentRetrofit = retrofit
            if (currentRetrofit != null) {
                return currentRetrofit.create(ApiService::class.java)
            }
            throw IllegalStateException("ApiClient не инициализирован. Вызовите ApiClient.init(url)")
        }

    fun init(url: String) {
        val newUrl = if (url.endsWith("/")) url else "$url/"
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        if (newUrl != baseUrl) {
            baseUrl = newUrl
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}