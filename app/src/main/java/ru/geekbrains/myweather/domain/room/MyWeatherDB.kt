package ru.geekbrains.myweather.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class MyWeatherDB : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}