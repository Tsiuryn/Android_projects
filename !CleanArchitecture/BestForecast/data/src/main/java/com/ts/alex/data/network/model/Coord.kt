package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)

internal fun Coord.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Coord{
    return com.ts.alex.domain.model.forecast.Coord(
        lat = this.lat,
        lon = this.lon
    )
}

