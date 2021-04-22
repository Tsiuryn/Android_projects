package com.ts.alex.bestforecast.di

import com.ts.alex.bestforecast.device.job.SyncJob
import com.ts.alex.bestforecast.ui.forecast.city_screen.CityViewModel
import com.ts.alex.bestforecast.ui.forecast.main_screen.ForecastMainViewModel
import com.ts.alex.bestforecast.ui.forecast.settings.SettingsViewModel
import com.ts.alex.bestforecast.ui.registration.RegistrationViewModel
import com.ts.alex.bestforecast.ui.start.StartViewModel
import com.ts.alex.data.datasource.IDataBaseDataSource
import com.ts.alex.data.datasource.IGetForecastDataSource
import com.ts.alex.data.datasource.IUserDataSource
import com.ts.alex.data.datasource.IWorkWithCityDataSource
import com.ts.alex.data.datasource.impl.DataBaseDataSource
import com.ts.alex.data.datasource.impl.GetForecastDataSource
import com.ts.alex.data.datasource.impl.UserDataSource
import com.ts.alex.data.datasource.impl.WorkWithCityDataSource
import com.ts.alex.data.local.database.CityDB
import com.ts.alex.data.local.prererences.SharedPreference
import com.ts.alex.data.network.RestApi
import com.ts.alex.data.network.providePlaceHolderApi
import com.ts.alex.data.repository.DataBaseRepository
import com.ts.alex.data.repository.GetForecastRepository
import com.ts.alex.data.repository.UserRepository
import com.ts.alex.data.repository.WorkWithCityRepository
import com.ts.alex.domain.usecase.IDataBaseUseCase
import com.ts.alex.domain.usecase.IGetForecastUseCase
import com.ts.alex.domain.usecase.IUserUseCase
import com.ts.alex.domain.usecase.IWorkWithCityUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val singleModule = module {
    viewModel { StartViewModel() }
    viewModel { RegistrationViewModel(get()) }
    viewModel {
        ForecastMainViewModel(
            context = get(),
            getForecast = get(),
            prefUser = get(),
            prefCity = get()
        )
    }
    viewModel { SettingsViewModel(iGetUser = get(), shp = get()) }
    viewModel { CityViewModel(getForecast = get(), db = get(), shp = get()) }

    factory { UserRepository(iUserDataSource = get()) as IUserUseCase }
    factory { GetForecastRepository(iGetForecastDataSource = get()) as IGetForecastUseCase }
    factory { WorkWithCityRepository(workWithCityDataSource = get()) as IWorkWithCityUseCase }
    factory { DataBaseRepository(idbDataSource = get()) as IDataBaseUseCase }

    factory { UserDataSource(sharedPreference = get()) as IUserDataSource }
    factory { GetForecastDataSource(restApi = get()) as IGetForecastDataSource }
    factory { WorkWithCityDataSource(preference = get()) as IWorkWithCityDataSource }
    single { DataBaseDataSource(db = get()) as IDataBaseDataSource }

    single { SharedPreference(get()) }
    single { providePlaceHolderApi() as RestApi }
    single { CityDB.getGoodJobDB(context = get())}
    single { SyncJob() }
}
