package ru.geekbrains.myweather.utlis

import ru.geekbrains.myweather.domain.room.HistoryEntity
import ru.geekbrains.myweather.repository.*

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

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature,weather.feelsLike, weather.icon)
}
