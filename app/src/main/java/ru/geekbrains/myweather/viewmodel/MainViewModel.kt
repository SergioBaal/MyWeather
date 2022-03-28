package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myweather.repository.Repository
import ru.geekbrains.myweather.repository.RepositoryImpl
import ru.geekbrains.myweather.repository.Weather
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeather() {
        Thread {
            liveData.postValue(AppState.Loading)
            if ((0..10).random() > 5) {
                liveData.postValue(AppState.Success(repository.getWeatherFromServer()))
            } else {
                liveData.postValue(AppState.Error(IllegalAccessException()))
            }

        }.start()
    }

}