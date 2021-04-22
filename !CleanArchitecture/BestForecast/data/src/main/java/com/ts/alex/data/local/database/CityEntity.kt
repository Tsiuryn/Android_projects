package com.ts.alex.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ts.alex.domain.model.City

@Entity(tableName = "db_city")
data class CityEntity (
    val name: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

internal fun CityEntity.convertToDomainEntity(): City {
    return City(
        name = this.name,
        id = this.id
    )
}

internal fun City.convertToDataEntity(): CityEntity {
    return CityEntity(
        name = this.name

    )
}