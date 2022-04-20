package ru.geekbrains.myweather.viewmodel

import ru.geekbrains.myweather.repository.Weather


sealed class WeatherListAppState {
    object Loading : WeatherListAppState()
    data class Success(val weatherList: List<Weather>) : WeatherListAppState()
    data class Error(val error: Throwable) : WeatherListAppState()
}

