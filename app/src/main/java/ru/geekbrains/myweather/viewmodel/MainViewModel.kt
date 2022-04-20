package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myweather.repository.RepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<WeatherListAppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {

    fun getData(): LiveData<WeatherListAppState> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)


    fun getWeather(isRussian: Boolean) {
        Thread {
            if ((0..10).random() > 5) {
                liveData.postValue(WeatherListAppState.Loading)
                val answer =
                    if (!isRussian) repository.getWorldWeatherFromLocalStorage() else repository.getRussianWeatherFromLocalStorage()
                liveData.postValue(WeatherListAppState.Success(answer))
            } else
                liveData.postValue(WeatherListAppState.Error(IllegalAccessException()))
        }.start()
    }

}