package ru.geekbrains.myweather.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class DetailsAppState {
    object Success : DetailsAppState()
    object Error : DetailsAppState()
}