package com.ts.alex.domain.model

data class Countries(
    val country: String,
    val totalRecovered: Int,
    val totalConfirmed: Int,
    val totalDeath: Int,
    val lat: Double,
    val long: Double
)