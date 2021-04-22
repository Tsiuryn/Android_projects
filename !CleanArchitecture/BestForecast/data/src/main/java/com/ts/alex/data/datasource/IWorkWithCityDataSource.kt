package com.ts.alex.data.datasource

interface IWorkWithCityDataSource {
    fun getCity(): String
    fun saveCity(city: String)
}