package ru.geekbrains.myweather.viewmodel

sealed class AppState {
    object Loading: AppState()
    data class Success(var weatherData: Any) : AppState()
    data class Error(val error: Throwable) : AppState()
}