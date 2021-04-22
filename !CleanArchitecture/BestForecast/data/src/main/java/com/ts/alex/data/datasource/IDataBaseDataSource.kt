package com.ts.alex.data.datasource

import com.ts.alex.data.local.database.CityEntity
import kotlinx.coroutines.flow.Flow

interface IDataBaseDataSource {
    suspend fun getAllCity (): Flow<List<CityEntity>>
    suspend fun addCity (cityEntity: CityEntity)
    suspend fun deleteCityById(id: Int)
    suspend fun updateCity(cityEntity: CityEntity)
}