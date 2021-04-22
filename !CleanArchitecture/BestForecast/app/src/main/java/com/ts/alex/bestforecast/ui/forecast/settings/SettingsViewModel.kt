package com.ts.alex.bestforecast.ui.forecast.settings

import androidx.lifecycle.ViewModel
import com.ts.alex.domain.usecase.IUserUseCase
import com.ts.alex.domain.usecase.IWorkWithCityUseCase


class SettingsViewModel(
    private val iGetUser: IUserUseCase,
    private val shp: IWorkWithCityUseCase
) : ViewModel() {

    var name = "Name: not registered"
        private set
    var email = "Email: "
        private set
    var password = "Password: "
        private set

    var defaultCity = "Default city: "


    init {
        val user = iGetUser.getUser()
        name = "Name:  ${if (user.name.isEmpty()) "you aren't registered" else user.name}"
        email = "Email: ${user.email}"
        password = "Password: ${user.password}"
        defaultCity = "Default city: ${shp.getCity()}"

    }
}