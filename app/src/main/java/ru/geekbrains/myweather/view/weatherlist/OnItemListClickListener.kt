package com.gb.k_1919_2.view.weatherlist


import ru.geekbrains.myweather.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}