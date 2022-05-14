package ru.geekbrains.myweather.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
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
                Log.d("@@@", " ошибка в detailsViewModel 1" )
            }
        })


    }

    fun isInternet(): Boolean {
       return true
    }


    interface Callback {
        fun onResponse(weather: Weather)


        fun onFail() {
            with (DetailsViewModel()) {
                liveData.postValue(DetailsState.Error(Throwable()))
                Log.d("@@@", " ошибка в detailsViewModel 2" )
            }
        }
    }


}