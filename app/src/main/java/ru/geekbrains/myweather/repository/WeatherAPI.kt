package ru.geekbrains.myweather.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.geekbrains.myweather.utlis.KEY_BUNDLE_LAT
import ru.geekbrains.myweather.utlis.KEY_BUNDLE_LON
import ru.geekbrains.myweather.utlis.X_API_KEY
import ru.geekbrains.myweather.utlis.YANDEX_PATH

interface WeatherAPI {
    @GET(YANDEX_PATH)
    fun getWeather(
        @Header(X_API_KEY) apikey: String,
        @Query(KEY_BUNDLE_LAT) lat: Double,
        @Query(KEY_BUNDLE_LON) lon: Double
    ): Call<WeatherDTO>
}