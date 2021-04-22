package com.ts.alex.data.network.model

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)

internal fun Clouds.convertToDomainEntity(): com.ts.alex.domain.model.forecast.Clouds{
    return com.ts.alex.domain.model.forecast.Clouds(
        all = this.all,
    )
}

