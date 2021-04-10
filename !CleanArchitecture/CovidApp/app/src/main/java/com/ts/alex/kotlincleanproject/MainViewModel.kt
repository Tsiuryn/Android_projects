package com.ts.alex.kotlincleanproject

import androidx.lifecycle.*
import com.ts.alex.domain.model.CovidModel
import com.ts.alex.domain.usecase.IGetFullInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class MainViewModel(
    private val iGetFullInformation: IGetFullInformation
) : ViewModel() {

    private var _getInformation = MutableLiveData<CovidModel>()
    val getInformation: LiveData<CovidModel>
        get() = _getInformation

    private var _onMapReady = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val information = iGetFullInformation.getInformation()
                withContext(Dispatchers.Main) {
                    _getInformation.value = information
                }
            }

        }
    }

    fun updateMapStatus(onMapReady: Boolean) {
        _onMapReady.value = onMapReady
    }

    val mapInfo: LiveData<CovidModel> =
        MediatorLiveData<CovidModel>().apply {
            val observer = Observer<Any?> {
                val information = _getInformation.value
                val onReadyMap = _onMapReady.value
                if (information != null && onReadyMap!!) {
                    value = information
                }


            }
            addSource(_getInformation, observer)
            addSource(_onMapReady, observer)
        }
}