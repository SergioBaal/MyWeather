package ru.geekbrains.myweather.repository

interface Repository {
    fun getWeatherFromServer():Weather
    fun getWeatherFromLocalStorage():Weather
}