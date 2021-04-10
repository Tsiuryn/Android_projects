package com.ts.alex.domain.model

data class CovidModel(
    val lastUpdate: String,
    val confirmed: String,
    val death: String,
    val recovered: String,
    val listSortedCountries: List<Countries>
)