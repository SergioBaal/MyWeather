package ru.geekbrains.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.myweather.repository.City
import ru.geekbrains.myweather.repository.DetailsRepository
import ru.geekbrains.myweather.repository.DetailsRepositoryOkHttpImpl
import ru.geekbrains.myweather.repository.Weather

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryOkHttpImpl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFail() {
                //  TODO HW   liveData.postValue(DetailsState.Error()) ("Not yet implemented")
            }
        })
    }


    interface Callback {
        fun onResponse(weather: Weather)

        // TODO HW Fail
        fun onFail()
    }


}
