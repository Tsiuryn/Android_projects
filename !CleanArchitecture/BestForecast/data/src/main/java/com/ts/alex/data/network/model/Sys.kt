package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    val country: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("sunrise")
    val sunrise: Int,

    @SerializedName("sunset")
    val sunset: Int,

    @SerializedName("type")
    val type: Int
)

internal fun Sys.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Sys{
    return com.ts.alex.domain.model.forecast.Sys(
        country = this.country,
        id = this.id,
        sunrise = this.sunrise,
        sunset = this.sunset,
        type = this.type
    )
}