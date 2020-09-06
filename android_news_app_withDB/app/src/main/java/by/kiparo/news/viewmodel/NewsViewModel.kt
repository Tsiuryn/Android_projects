package by.kiparo.news.viewmodel

import android.app.Application
import androidx.lifecycle.*
import by.kiparo.news.repository.Repository
import by.kiparo.news.database.News
import kotlinx.coroutines.launch


class NewsViewModel(repository: Repository): ViewModel() {
    private var mutableListNews = MutableLiveData<List<News>>()
    val listNews: LiveData<List<News>> = mutableListNews
    init {
        viewModelScope.launch {
            mutableListNews.value = repository.getPost()

        }
    }
}

