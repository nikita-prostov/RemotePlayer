package com.nks.interactive.remoteplayer.controller.api

import com.nks.interactive.remoteplayer.controller.models.TrackInfo
import com.nks.interactive.remoteplayer.controller.models.ServerState
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("switch")
    suspend fun switch(@Query("to") direction: String): TrackInfo

    @POST("volume/set")
    suspend fun setVolume(@Query("value") value: Float): Response<ResponseBody>

    @POST("play")
    suspend fun play(): Response<TrackInfo?>?

    @POST("play")
    suspend fun play(@Body trackInfo: TrackInfo? = null): TrackInfo

    @POST("shuffle")
    suspend fun shuffle(): ResponseBody

    @POST("sort")
    suspend fun sort(): ResponseBody

    @POST("pause")
    suspend fun pause(): ResponseBody

    @POST("load")
    suspend fun load(
        @Query("count") count: Int = 100,
        @Query("page") page: Int = 1
    ): List<TrackInfo>

    @GET("all")
    suspend fun getAll(
        @Query("count") count: Int = 100,
        @Query("page") page: Int = 1
    ): List<TrackInfo>

    @GET("track/current")
    suspend fun getCurrent(): Response<TrackInfo>

    @GET("state")
    suspend fun getState(): ServerState
}