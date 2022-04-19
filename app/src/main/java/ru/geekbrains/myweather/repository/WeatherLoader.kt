package ru.geekbrains.myweather.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {


    fun loadWeather(lat: Double, lon: Double) {
        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        Thread {
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    addRequestProperty("X-Yandex-API-Key", "2100c65a-d8e5-4b06-b0d3-0b95752799eb")
                }
            try {
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    onServerResponseListener.onResponse(weatherDTO)
                }
            } catch (e: Exception) {

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