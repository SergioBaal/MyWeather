package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData:MutableLiveData<Any> = MutableLiveData()):ViewModel() {

    fun getData (): LiveData <Any>{
        return liveData
    }

    fun getWeather() {
        Thread{
            sleep(2000L)
            liveData.postValue(Any())
        }.start()
    }

}