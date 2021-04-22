package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feels_like: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("pressure")
    val pressure: Int,

    @SerializedName("temp")
    val temp: Double,

    @SerializedName("temp_max")
    val temp_max: Double,

    @SerializedName("temp_min")
    val temp_min: Double
)

internal fun Main.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Main{
    return com.ts.alex.domain.model.forecast.Main(
        feels_like = this.feels_like,
        humidity = this.humidity,
        pressure = this.pressure,
        temp = this.temp,
        temp_max = this.temp_max,
        temp_min = this.temp_min
    )
}