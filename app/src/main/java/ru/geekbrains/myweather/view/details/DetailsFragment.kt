package ru.geekbrains.myweather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.k_1919_2.view.weatherlist.WeatherListFragment
import ru.geekbrains.myweather.databinding.FragmentDetailsBinding
import ru.geekbrains.myweather.repository.OnServerResponse
import ru.geekbrains.myweather.repository.Weather
import ru.geekbrains.myweather.repository.WeatherDTO
import ru.geekbrains.myweather.repository.WeatherLoader
import ru.geekbrains.myweather.utlis.KEY_BUNDLE_WEATHER


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


    lateinit var currentCityName : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
                WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
        }

    }

    private fun renderData(weather: WeatherDTO) {

        with (binding) {
                loadingLayout.visibility = View.GONE
                cityName.text = currentCityName
            with(weather) {
                temperatureValue.text = factDTO.temperature.toString()
                feelsLikeValue.text = factDTO.feelsLike.toString()
                cityCoordinates.text = "${infoDTO.lat} ${infoDTO.lon}"
            }
                with(WeatherListFragment()){
                mainView.showSnackBar("Успех", 2000)
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

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }


}

