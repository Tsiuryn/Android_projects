package com.ts.alex.data.model
import com.google.gson.annotations.SerializedName
import com.ts.alex.data.util.changeTime
import com.ts.alex.data.util.changingNumbers
import com.ts.alex.data.util.sortListForInfoFragment
import com.ts.alex.domain.model.CovidModel

data class Covid(
    @SerializedName("totalConfirmed")
    val totalConfirmed: Int,

    @SerializedName("totalDeaths")
    val totalDeaths: Int,

    @SerializedName("totalRecovered")
    val totalRecovered: Int,

    @SerializedName("data")
    val data: List<Datum>
)

