package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData:MutableLiveData<AppState> = MutableLiveData()):ViewModel() {

    fun getData (): LiveData <AppState>{
        return liveData
    }

    fun getWeather() {
        Thread{
            liveData.postValue(AppState.Loading)
            sleep(2000L)
            liveData.postValue(AppState.Success(Any()))
        }.start()
    }

}