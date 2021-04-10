package com.ts.alex.kotlincleanproject.modules

import com.ts.alex.data.network.RemoteDataSource
import com.ts.alex.data.network.impl.IRemoteDataSource
import com.ts.alex.data.repository.Repository
import com.ts.alex.data.retrofit.providePlaceHolderApi
import com.ts.alex.domain.usecase.IGetFullInformation
import com.ts.alex.kotlincleanproject.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { MainViewModel(get()) }
}

val restApi = module {
    single { providePlaceHolderApi() }
}

val dataSource = module {
    factory { RemoteDataSource(httpApi = get()) as IRemoteDataSource }
}

val repository = module {
    factory { Repository(iRemoteDataSource = get()) as IGetFullInformation }

}
