package ru.geekbrains.myweather.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.geekbrains.myweather.BuildConfig
import ru.geekbrains.myweather.utlis.X_API_KEY
import ru.geekbrains.myweather.utlis.YANDEX_DOMAIN
import ru.geekbrains.myweather.utlis.YANDEX_DOMAIN_HARD_MODE
import ru.geekbrains.myweather.utlis.YANDEX_PATH
import ru.geekbrains.myweather.viewmodel.DetailsAppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {


    fun loadWeather(lat: Double, lon: Double) {
       // val urlText = "$YANDEX_DOMAIN${YANDEX_PATH}lat=$lat&lon=$lon" //"https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val urlText = "$YANDEX_DOMAIN_HARD_MODE${YANDEX_PATH}lat=$lat&lon=$lon"   //"http://212.86.114.27/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        Thread {
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    addRequestProperty(X_API_KEY, BuildConfig.WEATHER_API_KEY)
                }

            try {
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when (responseCode) {
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val trueWeatherDTO: WeatherDTO? = Gson().fromJson(buffer, WeatherDTO::class.java)
                        isTrueAnswer(true, trueWeatherDTO)
                    }
                    in clientside -> {
                        Log.i("@@@", "Ошибка на стороне клиента $responseMessage")
                        isTrueAnswer(false, null)
                    }
                    in serverside -> {
                        Log.i("@@@", "Ошибка на стороне сервера $responseMessage")
                        isTrueAnswer(false,null)
                    }
                }
            } catch (e: NullPointerException) {
                Log.i("@@@", "Ошибка NullPointerException: ${e.message}")
                isTrueAnswer(false, null)

            } catch (e: JsonSyntaxException) {
                Log.i("@@@", "Ошибка: ${e.message}")
                isTrueAnswer(false, null)
            }
            finally {
                urlConnection.disconnect()
            }
        }.start()
    }

    private fun isTrueAnswer (boolean: Boolean, trueOrFalseWeatherDTO : WeatherDTO?) {
        if (boolean) {
            Handler(Looper.getMainLooper()).post {
                onServerResponseListener.onResponse(trueOrFalseWeatherDTO)
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                onServerResponseListener.onResponse(trueOrFalseWeatherDTO)
            }
        }

    }
}