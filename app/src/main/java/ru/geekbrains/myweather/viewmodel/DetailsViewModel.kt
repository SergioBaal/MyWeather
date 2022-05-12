package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myweather.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepositoryOne = DetailsRepositoryRetrofit2Impl(),
            private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
            }

            override fun onFail() {
                liveData.postValue(DetailsState.Error(Throwable()))
            }
        })
    }


    interface Callback {
        fun onResponse(weather: Weather)
        fun onFail()
    }

    interface CallbackAll {
        fun onResponse(listWeather: List <Weather>)
        fun onFail()
    }


}
