package com.ts.alex.data.model

import com.google.gson.annotations.SerializedName
import com.ts.alex.data.util.changeTime
import com.ts.alex.data.util.changingNumbers
import com.ts.alex.data.util.sortListForInfoFragment
import com.ts.alex.domain.model.CovidModel

data class Post(
    @SerializedName("lastCheckTimeMilli")
    val lastCheckTimeMilli: Long,

    @SerializedName("china")
    val covid: Covid
)

internal fun Post.convertToCovidModel(): CovidModel {
    return CovidModel(
        lastUpdate = changeTime(this.lastCheckTimeMilli),
        confirmed = changingNumbers(this.covid.totalConfirmed),
        death = changingNumbers(this.covid.totalDeaths),
        recovered = changingNumbers(this.covid.totalRecovered),
        listSortedCountries = sortListForInfoFragment(this.covid.data as ArrayList<Datum>)
    )
}