package com.ts.alex.data.datasource

import com.ts.alex.data.network.model.ForecastResponse

interface IGetForecastDataSource {
    suspend fun getWeatherByLocation(lat: Double, lon: Double): ForecastResponse
    suspend fun getWeatherByCity(city: String): ForecastResponse
}