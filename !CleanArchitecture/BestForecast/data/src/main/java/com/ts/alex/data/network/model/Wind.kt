package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class Wind(

    @SerializedName("deg")
    val deg: Int,

    @SerializedName("speed")
    val speed: Int
)

internal fun Wind.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Wind{
    return com.ts.alex.domain.model.forecast.Wind(
        deg = this.deg,
        speed = this.speed
    )
}