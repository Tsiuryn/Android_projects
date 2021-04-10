package com.ts.alex.data.repository

import com.ts.alex.data.network.impl.IRemoteDataSource
import com.ts.alex.domain.model.CovidModel
import com.ts.alex.domain.usecase.IGetFullInformation

class Repository(
    private val iRemoteDataSource: IRemoteDataSource
): IGetFullInformation {
    override suspend fun getInformation(): CovidModel {
        return iRemoteDataSource.getInformation()
    }

}