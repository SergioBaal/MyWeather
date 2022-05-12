package ru.geekbrains.myweather.repository

import ru.geekbrains.myweather.MyApp
import ru.geekbrains.myweather.utlis.convertHistoryEntityToWeather
import ru.geekbrains.myweather.utlis.convertWeatherToEntity
import ru.geekbrains.myweather.viewmodel.DetailsViewModel
import ru.geekbrains.myweather.viewmodel.HistoryViewModel


class DetailsRepositoryRoomImpl:DetailsRepositoryOne,DetailsRepositoryAll,DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list =convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
        if(list.isEmpty()){
            callback.onFail() // то и отобразить нечего
        }else{
            callback.onResponse(list.last()) //  hack
        }

    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
    }

}