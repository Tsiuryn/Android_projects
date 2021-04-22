package com.ts.alex.domain.usecase

interface IWorkWithCityUseCase {
    fun getCity(): String
    fun setCity(city: String)
}