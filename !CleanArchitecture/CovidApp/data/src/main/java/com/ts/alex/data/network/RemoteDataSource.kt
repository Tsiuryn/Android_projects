package com.ts.alex.data.network

import com.ts.alex.data.model.convertToCovidModel
import com.ts.alex.data.network.impl.IRemoteDataSource
import com.ts.alex.data.retrofit.RestApi
import com.ts.alex.domain.model.CovidModel

class RemoteDataSource(
    private val httpApi: RestApi
): IRemoteDataSource {

    override suspend fun getInformation(): CovidModel {
        val postRequest = httpApi.getPost().await()
        return postRequest.convertToCovidModel()
    }
}