package ru.geekbrains.myweather.viewmodel

import ru.geekbrains.myweather.repository.Weather

sealed class DetailsState {
    object Loading : DetailsState()
    data class Success(val weather: Weather) : DetailsState()
    data class Error(val error: Throwable) : DetailsState()
}