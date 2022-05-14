package ru.geekbrains.myweather

import android.app.Application
import androidx.room.Room
import ru.geekbrains.myweather.domain.room.HistoryDao
import ru.geekbrains.myweather.domain.room.MyWeatherDB

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var db: MyWeatherDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                if (appContext != null) {
                    db = Room.databaseBuilder(appContext!!, MyWeatherDB::class.java, "olo")
                        .allowMainThreadQueries().build() //TODO что-то не получилось с потоком
                } else {
                    throw IllegalStateException("что-то пошло не так")
                }
            }
            return db!!.historyDao()
        }
    }
}