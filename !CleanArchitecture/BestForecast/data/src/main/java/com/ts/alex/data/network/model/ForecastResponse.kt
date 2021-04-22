package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("base")
    val base: String,

    @SerializedName("clouds")
    val clouds: Clouds,

    @SerializedName("cod")
    val cod: Int,

    @SerializedName("coord")
    val coord: Coord,

    @SerializedName("dt")
    val dt: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: Main,

    @SerializedName("name")
    val name: String,

    @SerializedName("sys")
    val sys: Sys,

    @SerializedName("timezone")
    val timezone: Int,

    @SerializedName("visibility")
    val visibility: Int,

    @SerializedName("weather")
    val weather: List<Weather>,

    @SerializedName("wind")
    val wind: Wind
)

internal fun ForecastResponse.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Forecast {
    return com.ts.alex.domain.model.forecast.Forecast(
        base = this.base,
        clouds = this.clouds.convertToDomainEntity(),
        cod = this.cod,
        coord = this.coord.convertToDomainEntity(),
        dt = this.dt,
        id = this.id,
        main = this.main.convertToDomainEntity(),
        name = this.name,
        sys = this.sys.convertToDomainEntity(),
        timezone = this.timezone,
        visibility = this.visibility,
        weather = this.weather.map { it.convertToDomainEntity() },
        wind = this.wind.convertToDomainEntity()
    )
}