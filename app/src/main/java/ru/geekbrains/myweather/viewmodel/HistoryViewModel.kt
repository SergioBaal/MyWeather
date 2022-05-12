package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myweather.repository.DetailsRepositoryRoomImpl
import ru.geekbrains.myweather.repository.Weather

import java.lang.Thread.sleep

class HistoryViewModel(
    private val liveData: MutableLiveData<WeatherListAppState> = MutableLiveData(),
    private val repository: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl()
) :
    ViewModel() {

    fun getData(): LiveData<WeatherListAppState> {
        return liveData
    }

    fun getAll(){
        repository.getAllWeatherDetails(object : CallbackForAll{
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(WeatherListAppState.Success(listWeather))
            }

            override fun onFail() {
                TODO("Not yet implemented")
            }

        })
    }

    interface CallbackForAll {
        fun onResponse(listWeather: List<Weather>)
        // TODO HW Fail
        fun onFail()
    }


}
