package ru.geekbrains.myweather.repository

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}