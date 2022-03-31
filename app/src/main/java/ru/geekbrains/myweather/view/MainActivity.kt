package ru.geekbrains.myweather.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gb.k_1919_2.view.weatherlist.WeatherListFragment
import ru.geekbrains.myweather.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }

    }

}