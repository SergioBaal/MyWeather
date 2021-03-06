package ru.geekbrains.myweather.repository

import ru.geekbrains.myweather.viewmodel.DetailsViewModel

interface DetailsRepositoryOne {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}