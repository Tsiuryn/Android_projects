package com.ts.alex.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CityDAO {

    @Insert
    abstract suspend  fun addCity (cityEntity: CityEntity)

    @Update
    abstract suspend  fun updateCity(cityEntity: CityEntity)

    @Delete
    abstract suspend  fun deleteCity(cityEntity: CityEntity)

    @Query("SELECT * FROM db_city ORDER BY id DESC")
    abstract fun getAllCity(): Flow<List<CityEntity>>

    @Query("DELETE FROM db_city WHERE id ==:id ")
    abstract suspend  fun deleteCityById(id: Int)
}