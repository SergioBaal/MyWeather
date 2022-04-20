package ru.geekbrains.myweather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.k_1919_2.view.weatherlist.WeatherListFragment

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_list.*
import ru.geekbrains.myweather.databinding.FragmentDetailsBinding
import ru.geekbrains.myweather.repository.OnServerResponse
import ru.geekbrains.myweather.repository.Weather
import ru.geekbrains.myweather.repository.WeatherDTO
import ru.geekbrains.myweather.repository.WeatherLoader
import ru.geekbrains.myweather.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.myweather.viewmodel.DetailsAppState


class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    lateinit var currentCityName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
        }

    }

    private fun renderData(weather: WeatherDTO?, detailsAppState: DetailsAppState) {

        when (detailsAppState) {
            is DetailsAppState.Success -> {

                with(binding) {
                    with(weather!!) {
                        loadingLayout.visibility = android.view.View.GONE
                        cityName.text = currentCityName
                        temperatureValue.text = factDTO.temperature.toString()
                        feelsLikeValue.text = factDTO.feelsLike.toString()
                        cityCoordinates.text = "${infoDTO.lat} ${infoDTO.lon}"
                    }
                }

            }
            is DetailsAppState.Error -> {
                with(binding) {
                    fragmentDetails.showSnackBar(
                        "Ошибка!",
                        "Повторить?",
                        { arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
                            currentCityName = it.city.name
                            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
                        } })
                }
            }

        }




    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO?, detailsAppState: DetailsAppState) {
        renderData(weatherDTO, detailsAppState)
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


