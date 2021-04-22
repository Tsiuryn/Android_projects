package com.ts.alex.domain.usecase

import com.ts.alex.domain.model.forecast.Forecast

interface IGetForecastUseCase {
    suspend fun getWeatherByLocation(lat: Double, lon: Double): Forecast

    suspend fun getWeatherByCity(city: String):Forecast
}