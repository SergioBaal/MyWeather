package ru.geekbrains.myweather.repository

import ru.geekbrains.myweather.viewmodel.DetailsViewModel

interface DetailsRepositoryAdd {
    fun addWeather(weather: Weather)
}