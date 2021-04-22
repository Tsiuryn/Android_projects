package com.ts.alex.data.network

import com.ts.alex.data.local.BuildConfig.API_KEY
import com.ts.alex.data.network.model.ForecastResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {

    @GET("data/2.5/weather")
    fun getWeatherByCity (
        @Query("q") city: String,
        @Query("appid") key: String? = API_KEY,
        @Query("units") units: String? = "metric"
    ): Deferred<ForecastResponse>

    @GET("data/2.5/weather")
    fun getWeatherByLocation (
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") key: String? = API_KEY,
        @Query("units") units: String? = "metric"
    ): Deferred<ForecastResponse>
}