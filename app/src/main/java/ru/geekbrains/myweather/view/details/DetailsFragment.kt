package ru.geekbrains.myweather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myweather.R
import ru.geekbrains.myweather.databinding.FragmentDetailsBinding
import ru.geekbrains.myweather.repository.Weather
import ru.geekbrains.myweather.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.myweather.viewmodel.DetailsState
import ru.geekbrains.myweather.viewmodel.DetailsViewModel


class DetailsFragment : Fragment() {

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

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    // lateinit var currentCityName: String // открыть в случае непреодолимого желания работать с WeatherLoader или DetailsService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(
            viewLifecycleOwner
        ) { t -> renderData(t) }

        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)

            /**
             * currentCityName = it.city.name
             * <Работа с WeatherLoader:>
             * WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
             */

            /**
             * Работа с сервисом:
             * LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(KEY_WAVE_SERVICE_BROADCAST))
             * requireActivity().startService(
            Intent(requireContext(), DetailsService::class.java).apply {
            putExtra(KEY_BUNDLE_LAT, it.city.lat)
            putExtra(KEY_BUNDLE_LON, it.city.lon)
            })
             */

            /**
             * Работа с OkHttp:
             */

        }
    }


    private fun renderData(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Error -> {
                with(binding) {
                    fragmentDetails.showSnackBar(
                        "Ошибка!",
                        "Повторить?",
                        {
                            arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
                                viewModel.getWeather(it.city)
                            }
                        })
                }
            }
            is DetailsState.Loading -> {}
            is DetailsState.Success -> {
                val weather = detailsState.weather
                with(binding) {
                    with(weather) {
                        loadingLayout.visibility = View.GONE
                        cityName.text = city.name
                        temperatureValue.text = temperature.toString()
                        feelsLikeValue.text = feelsLike.toString()
                        cityCoordinates.text = "${city.lat} ${city.lon}"
                    }
                    /* Glide.with(requireContext())
                         .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                         .into(headerCityIcon)*/

                    /*  Picasso.get()?.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                          ?.into(headerCityIcon) */

                    headerCityIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png") {
                        placeholder(R.drawable.ic_russia)
                        error(R.drawable.ic_earth)
                    }
                    icon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")


                }
            }
        }
    }

    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    /*  if (weather == null) {
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
  } */


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    /** Для работы с сервисом, не забыть занулить в onDestroy: LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
     *
     *  override fun onResponse(weatherDTO: WeatherDTO?) {
    renderData(weatherDTO)
    }
     * И создать ресивер:
     *  val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
    intent?.let {
    val weatherDTO =
    it.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
    onResponse(weatherDTO)
    }
    }
    }
     */


}

private fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}







