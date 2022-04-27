package ru.geekbrains.myweather.utlis

import ru.geekbrains.myweather.repository.FactDTO
import ru.geekbrains.myweather.repository.Weather
import ru.geekbrains.myweather.repository.WeatherDTO
import ru.geekbrains.myweather.repository.getDefaultCity

const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_DOMAIN_HARD_MODE = "http://212.86.114.27/"
const val YANDEX_PATH = "v2/informers?"
const val KEY_BUNDLE_WEATHER = "weather"
const val X_API_KEY = "X-Yandex-API-Key"
const val KEY_WAVE = "keyWave"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "weather_s_b"
const val KEY_WAVE_SERVICE_BROADCAST = "wave"
const val KEY_BUNDLE_LAT = "lat"
const val KEY_BUNDLE_LON = "lon"


class Utils {
}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}