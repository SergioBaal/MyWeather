package ru.geekbrains.myweather.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.geekbrains.myweather.BuildConfig
import ru.geekbrains.myweather.repository.WeatherDTO
import ru.geekbrains.myweather.utlis.*
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            Log.d("@@@", "work DetailsService $lat $lon")
            //val urlText = "$YANDEX_DOMAIN${YANDEX_PATH}lat=$lat&lon=$lon"//"https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
            val urlText = "$YANDEX_DOMAIN_HARD_MODE${YANDEX_PATH}lat=$lat&lon=$lon"  //"http://212.86.114.27/v2/informers?lat=$lat&lon=$lon"
            val uri = URL(urlText)
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    addRequestProperty(X_API_KEY, BuildConfig.WEATHER_API_KEY)
                }
            try {
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO? = Gson().fromJson(buffer, WeatherDTO::class.java)
                val responseMessage = urlConnection.responseMessage
                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when (urlConnection.responseCode) {
                    in responseOk -> {
                        answerFromService(weatherDTO)
                    }
                    in clientside -> {
                        Log.i("@@@", "Ошибка на стороне клиента $responseMessage")
                        answerFromService(null)
                    }
                    in serverside -> {
                        Log.i("@@@", "Ошибка на стороне сервера $responseMessage")
                        answerFromService(null)
                    }
                }
            } catch (e: NullPointerException) {
                Log.i("@@@", "Ошибка NullPointerException: ${e.message}")
                answerFromService(null)
            } catch (e: FileNotFoundException) {
                Log.i("@@@", "Ошибка FileNotFoundException: ${e.message}")
                answerFromService(null)
            } catch (e: JsonSyntaxException) {
                Log.i("@@@", "Ошибка: ${e.message}")
                answerFromService(null)
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    private fun answerFromService(weatherDTO: WeatherDTO?) {
        val message = Intent(KEY_WAVE_SERVICE_BROADCAST)
        message.putExtra(
            KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO
        )
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }
}


