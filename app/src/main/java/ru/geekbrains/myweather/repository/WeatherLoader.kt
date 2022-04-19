package ru.geekbrains.myweather.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {


    fun loadWeather(lat: Double, lon: Double) {
        // val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val urlText = "http://212.86.114.27/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        Thread {
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    addRequestProperty("X-Yandex-API-Key", "2100c65a-d8e5-4b06-b0d3-0b95752799eb")
                }
            try {
                val response = urlConnection.responseCode

                when (response) {
                    in 400..499 -> {
                    }
                    in 500..599 -> {
                    }
                    in 200..299 -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        Handler(Looper.getMainLooper()).post {
                            onServerResponseListener.onResponse(weatherDTO)
                        }
                    }
                }


            } catch (e: JsonSyntaxException) {
                Log.i("@@@", "Что-то пошло не так: ${e.message}")

            } finally {
                urlConnection.disconnect()
            }


            //  val responseCode = urlConnection.responseCode
            // if (responseCode == 200) {
            //      Log.i("@@@", "POLOLKA")
            //   }


        }.start()


    }

}