package ru.geekbrains.myweather.repository


import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: FactDTO,
    @SerializedName("forecast")
    val forecast: ForecastDTO,
    @SerializedName("info")
    val info: InfoDTO,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String
)