package ru.geekbrains.myweather.view.weatherlist


import ru.geekbrains.myweather.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}