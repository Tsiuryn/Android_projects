package com.ts.alex.data.datasource.impl

import com.ts.alex.data.datasource.IWorkWithCityDataSource
import com.ts.alex.data.local.prererences.SharedPreference

class WorkWithCityDataSource(private val preference: SharedPreference): IWorkWithCityDataSource {
    override fun getCity(): String {
        return preference.getCity()?: ""
    }

    override fun saveCity(city: String) {
        preference.saveCity(city)
    }
}