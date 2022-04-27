package ru.geekbrains.myweather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import ru.geekbrains.myweather.BuildConfig
import ru.geekbrains.myweather.databinding.FragmentDetailsBinding
import ru.geekbrains.myweather.repository.OnServerResponse
import ru.geekbrains.myweather.repository.Weather
import ru.geekbrains.myweather.repository.WeatherDTO
import ru.geekbrains.myweather.utlis.*
import java.io.IOException


class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val weatherDTO =
                    it.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
                onResponse(weatherDTO)
            }
        }
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
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(KEY_WAVE_SERVICE_BROADCAST))

        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            /**
             * <Работа с WeatherLoader:>
             * WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
             * */
            /**
             * Работа с сервисом:
             * requireActivity().startService(
            Intent(requireContext(), DetailsService::class.java).apply {
            putExtra(KEY_BUNDLE_LAT, it.city.lat)
            putExtra(KEY_BUNDLE_LON, it.city.lon)
            })
             */
            /**
             * Работа с OkHttp:
             */
            getWeather(it.city.lat, it.city.lon)
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        binding.loadingLayout.visibility = View.VISIBLE

        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(X_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$YANDEX_DOMAIN_HARD_MODE${YANDEX_PATH}lat=$lat&lon=$lon")
        val request = builder.build()

        val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                with (binding) {
                    fragmentDetails.showSnackBar(
                        "Ошибка!",
                        "Повторить?",
                        { getWeather(lat, lon) } )
                }

                binding.loadingLayout.visibility = View.GONE
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
                    requireActivity().runOnUiThread { renderData(weatherDTO) }
                } else {
                    with (binding) {
                        fragmentDetails.showSnackBar(
                            "Ошибка!",
                            "Повторить?",
                            { getWeather(lat, lon) } )
                    }

                }
            }
        }
        binding.loadingLayout.visibility = View.GONE

        val call = client.newCall(request)
        //call.execute()// синхронно
        call.enqueue(callback) //асинхронно

    }


    private fun renderData(weather: WeatherDTO?) {
        if (weather == null) {
            with(binding) {
                fragmentDetails.showSnackBar(
                    "Ошибка!",
                    "Повторить?",
                    {
                        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
                            currentCityName = it.city.name
                            /** вызываем включение WeatherLoader при нажатии на "повторить"
                             * WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)*/

                            /** вызываем включение сервиса при нажатии на "повторить" */
                            requireActivity().startService(
                                Intent(
                                    requireContext(),
                                    DetailsService::class.java
                                ).apply {
                                    putExtra(KEY_BUNDLE_LAT, it.city.lat)
                                    putExtra(KEY_BUNDLE_LON, it.city.lon)
                                })
                        }
                    })
            }
        } else {
            with(binding) {
                with(weather) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = currentCityName
                    temperatureValue.text = factDTO.temperature.toString()
                    feelsLikeValue.text = factDTO.feelsLike.toString()
                    cityCoordinates.text = "${infoDTO.lat} ${infoDTO.lon}"
                }
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

    override fun onResponse(weatherDTO: WeatherDTO?) {
        renderData(weatherDTO)
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


