package com.ts.alex.domain.usecase

import com.ts.alex.domain.model.CovidModel

interface IGetFullInformation {

    suspend fun getInformation(): CovidModel
}