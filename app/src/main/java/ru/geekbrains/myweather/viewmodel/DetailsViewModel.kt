package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myweather.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    private var repositoryOne: DetailsRepositoryOne = DetailsRepositoryRetrofit2Impl()


    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repositoryOne = if (isInternet()) {
            DetailsRepositoryRetrofit2Impl()
        } else {
            DetailsRepositoryRoomImpl()
        }
        repositoryOne.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                if (isInternet()){
                    repositoryAdd.addWeather(weather)
                }
            }

            override fun onFail() {
              liveData.postValue(DetailsState.Error(Throwable()))
            }
        })


    }

    private fun isInternet(): Boolean {
        //!!! заглушка
        return true
    }

    interface Callback {
        fun onResponse(weather: Weather)


        fun onFail() {
            with (DetailsViewModel()) {
                liveData.postValue(DetailsState.Error(Throwable()))
            }
        }
    }


}