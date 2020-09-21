package my.app.ts_pomodoro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.app.ts_pomodoro.viewmodel.repo.Repository

class TimerViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TimerViewModel(repository) as T
    }
}