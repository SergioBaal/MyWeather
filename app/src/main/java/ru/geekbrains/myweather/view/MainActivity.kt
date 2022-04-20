package ru.geekbrains.myweather.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gb.k_1919_2.view.weatherlist.WeatherListFragment
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myweather.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }

    }

}

private fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.showSnackBar(text: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
    this.let { Snackbar.make(it, text, length).show() }
}