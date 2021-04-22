package com.ts.alex.data.repository

import com.ts.alex.data.datasource.IDataBaseDataSource
import com.ts.alex.data.local.database.convertToDataEntity
import com.ts.alex.data.local.database.convertToDomainEntity
import com.ts.alex.domain.model.City
import com.ts.alex.domain.usecase.IDataBaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataBaseRepository (private val idbDataSource: IDataBaseDataSource): IDataBaseUseCase {

    override suspend fun getAllCity(): Flow<List<City>> {
        return idbDataSource.getAllCity().map { it.map { it.convertToDomainEntity() } }
    }

    override suspend fun addCity(city: City) {
        idbDataSource.addCity(city.convertToDataEntity())
    }

    override suspend fun deleteCityById(id: Int) {
        idbDataSource.deleteCityById(id)
    }

    override suspend fun updateCity(city: City) {
        idbDataSource.updateCity(city.convertToDataEntity())
    }
}