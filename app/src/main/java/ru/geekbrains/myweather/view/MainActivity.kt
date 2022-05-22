package ru.geekbrains.myweather.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.gb.k_1919_2.view.weatherlist.HistoryWeatherListFragment
import ru.geekbrains.myweather.R
import ru.geekbrains.myweather.lesson10.MapsFragment
import ru.geekbrains.myweather.lesson9.WorkWithContentProviderFragment
import ru.geekbrains.myweather.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).addToBackStack("")
                .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                val fragmentHistory = supportFragmentManager.findFragmentByTag("tag")
                if (fragmentHistory == null) {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(
                                R.id.container,
                                HistoryWeatherListFragment.newInstance(),
                                "tag"
                            )
                            .addToBackStack("")
                            .commit() //FIXME
                    }
                }
            }
            R.id.action_contacts -> {
                val fragmentContacts = supportFragmentManager.findFragmentByTag("contacts")
                if (fragmentContacts == null) {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(
                                R.id.container,
                                WorkWithContentProviderFragment.newInstance(),
                                "contacts"
                            )
                            .addToBackStack("")
                            .commit()
                    }
                }
            }
            R.id.action_menu_google_maps->{
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment()).addToBackStack("").commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}