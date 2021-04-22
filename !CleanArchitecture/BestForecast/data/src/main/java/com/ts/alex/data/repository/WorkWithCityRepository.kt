package com.ts.alex.data.repository

import com.ts.alex.data.datasource.IWorkWithCityDataSource
import com.ts.alex.domain.usecase.IWorkWithCityUseCase

class WorkWithCityRepository(private val workWithCityDataSource: IWorkWithCityDataSource)
    : IWorkWithCityUseCase{
    override fun getCity(): String {
        return workWithCityDataSource.getCity()
    }

    override fun setCity(city: String) {
        workWithCityDataSource.saveCity(city)
    }
}