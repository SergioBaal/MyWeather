package ru.geekbrains.myweather.repository

import ru.geekbrains.myweather.viewmodel.DetailsViewModel
import ru.geekbrains.myweather.viewmodel.HistoryViewModel

interface DetailsRepositoryAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll)
}