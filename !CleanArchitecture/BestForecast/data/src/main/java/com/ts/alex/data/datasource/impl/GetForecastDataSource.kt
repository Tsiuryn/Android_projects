package com.ts.alex.data.datasource.impl

import com.ts.alex.data.datasource.IGetForecastDataSource
import com.ts.alex.data.network.RestApi
import com.ts.alex.data.network.model.ForecastResponse

class GetForecastDataSource(private val restApi: RestApi): IGetForecastDataSource {
    override suspend fun getWeatherByLocation(lat: Double, lon: Double): ForecastResponse {
        return restApi.getWeatherByLocation(lat = lat, lon = lon).await()
    }

    override suspend fun getWeatherByCity(city: String): ForecastResponse {
        return restApi.getWeatherByCity(city = city).await()
    }

}