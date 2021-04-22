package com.ts.alex.data.datasource.impl

import com.ts.alex.data.datasource.IDataBaseDataSource
import com.ts.alex.data.local.database.CityDB
import com.ts.alex.data.local.database.CityEntity
import kotlinx.coroutines.flow.Flow

class DataBaseDataSource(private val db: CityDB): IDataBaseDataSource {
    private val dao = db.cityDAO()

    override suspend fun getAllCity(): Flow<List<CityEntity>> {
        return dao.getAllCity()
    }

    override suspend fun addCity(cityEntity: CityEntity) {
        dao.addCity(cityEntity)
    }

    override suspend fun deleteCityById(id: Int) {
        dao.deleteCityById(id)
    }

    override suspend fun updateCity(cityEntity: CityEntity) {
        dao.updateCity(cityEntity)
    }
}