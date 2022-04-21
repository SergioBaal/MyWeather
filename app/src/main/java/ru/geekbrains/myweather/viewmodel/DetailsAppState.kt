package ru.geekbrains.myweather.viewmodel

sealed class DetailsAppState {
    object Success : DetailsAppState()
    object Error : DetailsAppState()
}