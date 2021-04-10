package com.ts.alex.data.network.impl

import com.ts.alex.domain.model.CovidModel

interface IRemoteDataSource {
    suspend fun getInformation(): CovidModel
}