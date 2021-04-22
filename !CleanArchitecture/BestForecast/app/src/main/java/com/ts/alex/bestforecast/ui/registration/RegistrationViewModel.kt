package com.ts.alex.bestforecast.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ts.alex.bestforecast.ui.registration.util.isValidEmail
import com.ts.alex.bestforecast.ui.registration.util.isValidPassword
import com.ts.alex.bestforecast.ui.registration.util.isValidText
import com.ts.alex.domain.model.User
import com.ts.alex.domain.usecase.IUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class RegistrationViewModel(
    private val user: IUserUseCase
) : ViewModel() {

    var isValidName = false
        private set
    var isValidEmail = false
        private set
    var isValidPassword = false
        private set

    private var name = ""
    private var email = ""
    private var password = ""
    private var isSignUp = true

    private val _enableButton = MutableSharedFlow<Boolean>()
    val enableButton: Flow<Boolean>
        get() = _enableButton

    private val _goToTheNextStep = MutableSharedFlow<Boolean>()
    val goToTheNextStep: Flow<Boolean>
        get() = _goToTheNextStep

    private val _hideProgress = MutableSharedFlow<Boolean>()
    val hideProgress: Flow<Boolean>
        get() = _hideProgress

    fun setConfigScreen (isSignUp: Boolean){
        this.isSignUp = isSignUp
    }

    fun validationName(name: String) {
        isValidName = isValidText(name)
        this.name = name
        checkFields()
    }

    fun validationEmail(email: String) {
        isValidEmail = isValidEmail(email)
        this.email = email
        checkFields()
    }

    fun validationPassword(password: String) {
        isValidPassword = isValidPassword(password)
        this.password = password
        checkFields()
    }

    fun nextStep(isSignUp: Boolean) {
        viewModelScope.launch {
            delay(3_000)
            if(isSignUp){
                user.saveUser(User(name = name, email = email, password = password))
                _hideProgress.emit(true)
                _goToTheNextStep.emit(true)
            }else{
                _hideProgress.emit(true)
                _goToTheNextStep.emit(isCorrectData())
            }
        }
    }

    private fun isCorrectData(): Boolean {
        val user = user.getUser()
        return email == user.email && password == user.password
    }

    private fun checkFields() {
        val validation = if(isSignUp) isValidName && isValidEmail && isValidPassword else isValidEmail && isValidPassword
        viewModelScope.launch {
            _enableButton.emit(validation)
        }
    }
}