package com.ts.alex.bestforecast.ui.forecast.city_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ts.alex.domain.model.City
import com.ts.alex.domain.usecase.IDataBaseUseCase
import com.ts.alex.domain.usecase.IGetForecastUseCase
import com.ts.alex.domain.usecase.IWorkWithCityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CityViewModel(
    private val getForecast: IGetForecastUseCase,
    private val db: IDataBaseUseCase,
    private val shp: IWorkWithCityUseCase
): ViewModel() {

    private val TAG = CityViewModel::class.java.canonicalName

    private val _weatherByCity = MutableSharedFlow<Boolean>()
    val weatherByCity: Flow<Boolean>
        get() = _weatherByCity

    private val _clickAdd = MutableSharedFlow<Boolean>()
    val clickAdd: Flow<Boolean>
        get() = _clickAdd

    private val _listCity = MutableSharedFlow<List<City>>()
    val listCity: Flow<List<City>>
        get() = _listCity

    fun getWeatherByCity (city: String){
        viewModelScope.launch {
            try {
                val forecast = getForecast.getWeatherByCity(city)
                if(forecast.cod == 200){
                    _weatherByCity.emit(true)
                }
            }catch (e: Exception){
                _weatherByCity.emit(false)
            }

        }

    }

    fun getListCity(){
        viewModelScope.launch {
            try {
                db.getAllCity().collect {
                    _listCity.emit(it)
                }
            }catch (e: Exception){
                Log.e(TAG, "getListCity: empty db", )
            }

        }
    }

    fun addCity (city: String){
        viewModelScope.launch {
            db.addCity(City(name = city))
        }
    }

    fun saveCityInPreferences(city: String){
        shp.setCity(city)
    }

    fun deleteCity(city: City){
        viewModelScope.launch {
            db.deleteCityById(city.id)
        }
    }

    fun clickAdd(){
        viewModelScope.launch {
            _clickAdd.emit(true)
        }
    }
}