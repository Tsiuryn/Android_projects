package com.ts.alex.data.repository

import com.ts.alex.data.datasource.IGetForecastDataSource
import com.ts.alex.data.network.model.convertToDomainEntity
import com.ts.alex.domain.model.forecast.Forecast
import com.ts.alex.domain.usecase.IGetForecastUseCase

class GetForecastRepository(
    private val iGetForecastDataSource: IGetForecastDataSource
) : IGetForecastUseCase {
    override suspend fun getWeatherByLocation(lat: Double, lon: Double): Forecast {
        return iGetForecastDataSource.getWeatherByLocation(lat = lat, lon = lon)
            .convertToDomainEntity()
    }

    override suspend fun getWeatherByCity(city: String): Forecast {
        return iGetForecastDataSource.getWeatherByCity(city = city).convertToDomainEntity()
    }
}