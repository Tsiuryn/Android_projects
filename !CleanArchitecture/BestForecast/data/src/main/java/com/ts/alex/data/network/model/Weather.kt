package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class Weather(

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: String
)

internal fun Weather.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Weather{
    return com.ts.alex.domain.model.forecast.Weather(
        description = this.description,
        icon = this.icon,
        id = this.id,
        main = this.main
    )
}