package com.ts.alex.bestforecast.ui.forecast.main_screen

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ts.alex.bestforecast.device.gps.setUpLocationListener
import com.ts.alex.domain.model.forecast.Forecast
import com.ts.alex.domain.usecase.IGetForecastUseCase
import com.ts.alex.domain.usecase.IUserUseCase
import com.ts.alex.domain.usecase.IWorkWithCityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ForecastMainViewModel(
    private val context: Context,
    private val getForecast: IGetForecastUseCase,
    private val prefUser: IUserUseCase,
    private val prefCity: IWorkWithCityUseCase
) : ViewModel() {

    var myUser = "Hi,"

    var city = ""
    private set

    private var forecast : Forecast? = null


    private val _progress: MutableSharedFlow<Boolean> = MutableSharedFlow<Boolean>()
    val progress: Flow<Boolean> = _progress

    private val _error: MutableSharedFlow<Throwable> = MutableSharedFlow<Throwable>()
    val error: Flow<Throwable> = _error

    private val _weatherByCity = MutableSharedFlow<Forecast>()
    val weatherByCity: Flow<Forecast>
        get() = _weatherByCity

    private val _weatherByLocation = MutableSharedFlow<Forecast>()
    val weatherByLocation: Flow<Forecast>
        get() = _weatherByLocation

    private val _showException = MutableSharedFlow<String>()
    val showException: Flow<String>
        get() = _showException

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            handleLoading {
                forecast = getForecast.getWeatherByCity(city)
                _weatherByCity.emit(forecast!!)

            }
        }
    }

    fun getUser(){
        viewModelScope.launch {
            val user = prefUser.getUser().name
            myUser = "Hi, ${if(user.isEmpty()) "you aren't registered" else user}"
        }
    }

    @SuppressLint("MissingPermission")
    fun getWeatherByCurrentLocation() {
        getLocation {
            viewModelScope.launch(Dispatchers.IO) {

                handleLoading {
                    forecast = getForecast.getWeatherByLocation(
                        lat = it.latitude,
                        lon = it.longitude
                    )
                    _weatherByLocation.emit(forecast!!)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(listener: (Location) -> Unit) {
        viewModelScope.launch {
            _progress.emit(true)
                setUpLocationListener(context) {
                    listener(it)
                }
        }

    }

    fun saveData(){
        setDefaultCity(forecast?.name?: "")
    }

    fun getDefaultCity():String = prefCity.getCity()

    private fun setDefaultCity(city: String){
        prefCity.setCity(city)
    }

    private suspend inline fun handleLoading(
        block: () -> Unit
    ) {
        _progress.emit(true)
        try {
            block()
        } catch (ex: Throwable) {
            _error.emit(ex)
        } finally {
            _progress.emit(false)
        }
    }
}