package com.ts.alex.bestforecast.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class StartViewModel: ViewModel() {

    private val _startSignUp = MutableSharedFlow<Boolean>()
    val startSignUp: Flow<Boolean>
        get() = _startSignUp

    private val _startSignIn= MutableSharedFlow<Boolean>()
    val startSignIn: Flow<Boolean>
        get() = _startSignIn

    private val _startWithoutRegistr= MutableSharedFlow<Boolean>()
    val startWithoutRegistr: Flow<Boolean>
        get() = _startWithoutRegistr


    fun startSignUp(){
        viewModelScope.launch {
            _startSignUp.emit(true)
        }
    }

    fun startSignIn(){
        viewModelScope.launch {
            _startSignIn.emit(true)
        }
    }

    fun startWithoutRegistration(){
        viewModelScope.launch {
            _startWithoutRegistr.emit(true)
        }
    }
}