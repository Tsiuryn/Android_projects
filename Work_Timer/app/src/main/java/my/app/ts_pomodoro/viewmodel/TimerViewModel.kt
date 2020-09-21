package my.app.ts_pomodoro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.app.ts_pomodoro.my_database.GoodJobEntity
import my.app.ts_pomodoro.viewmodel.repo.Repository

class TimerViewModel (val repository: Repository): ViewModel() {
    fun addJobToDB (goodJobEntity: GoodJobEntity){
        viewModelScope.launch {
            repository.addGoodJobToDB(goodJobEntity)
        }
    }
}