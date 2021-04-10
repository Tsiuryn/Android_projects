package com.ts.alex.data.model

import com.google.gson.annotations.SerializedName

data class Datum(
    @SerializedName("country")
    val country: String,

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("long")
    val long: Double,

    @SerializedName("confirmed")
    var confirmed: Int,

    @SerializedName("deaths")
    val deaths: Int,

    @SerializedName("recovered")
    val recovered: Int
)