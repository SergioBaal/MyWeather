package ru.geekbrains.myweather.repository

import ru.geekbrains.myweather.viewmodel.DetailsAppState

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO?)
}