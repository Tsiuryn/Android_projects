package my.app.ts_pomodoro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.app.ts_pomodoro.classes.MyJobEntity
import my.app.ts_pomodoro.viewmodel.repo.Repository

class StatisticsViewModel(val repository: Repository): ViewModel() {
    private val mutableListFromDB = MutableLiveData<List<MyJobEntity>>()
    val listFromDB: LiveData<List<MyJobEntity>> = mutableListFromDB

    init {
        viewModelScope.launch {
            val list = repository.getListFromDB()
            if (list.isNotEmpty()){
                mutableListFromDB.value = list
            }
        }
    }



}