package ru.geekbrains.myweather.repository

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.geekbrains.myweather.BuildConfig
import ru.geekbrains.myweather.utlis.X_API_KEY
import ru.geekbrains.myweather.utlis.YANDEX_DOMAIN
import ru.geekbrains.myweather.utlis.YANDEX_PATH
import ru.geekbrains.myweather.utlis.convertDtoToModel
import ru.geekbrains.myweather.viewmodel.DetailsViewModel

class DetailsRepositoryOkHttpImpl : DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(X_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$YANDEX_DOMAIN${YANDEX_PATH}lat=${city.lat}&lon=${city.lon}")
        val request = builder.build()
        val call = client.newCall(request)
        Thread {
            val response = call.execute()
            if (response.isSuccessful) {
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            } else {
                //TODO HW
            }
        }.start()
    }
}