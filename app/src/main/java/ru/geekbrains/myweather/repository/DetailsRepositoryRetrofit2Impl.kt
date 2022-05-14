package ru.geekbrains.myweather.repository

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.myweather.BuildConfig
import ru.geekbrains.myweather.utlis.YANDEX_DOMAIN
import ru.geekbrains.myweather.utlis.convertDtoToModel
import ru.geekbrains.myweather.viewmodel.DetailsViewModel

val weatherAPI = Retrofit.Builder().apply {
    baseUrl(YANDEX_DOMAIN)
    addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
}.build().create(WeatherAPI::class.java)

class DetailsRepositoryRetrofit2Impl : DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weather = convertDtoToModel(it)
                            weather.city = city
                            callbackMy.onResponse(weather)
                        }
                    } else {
                        callbackMy.onFail()
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callbackMy.onFail()
                }

            })
    }
}