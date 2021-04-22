package com.ts.alex.domain.usecase

import com.ts.alex.domain.model.City
import kotlinx.coroutines.flow.Flow

interface IDataBaseUseCase {
    suspend fun getAllCity (): Flow<List<City>>
    suspend fun addCity (city: City)
    suspend fun deleteCityById(id: Int)
    suspend fun updateCity(city: City)
}