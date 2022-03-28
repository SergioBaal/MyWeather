package ru.geekbrains.myweather.repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000L)
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }
}