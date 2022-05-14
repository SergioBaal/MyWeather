package ru.geekbrains.myweather.view.weatherlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_list.*
import ru.geekbrains.myweather.R
import ru.geekbrains.myweather.databinding.FragmentWeatherListBinding
import ru.geekbrains.myweather.repository.Weather
import ru.geekbrains.myweather.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.myweather.utlis.KEY_SP_IS_RUSSIAN
import ru.geekbrains.myweather.view.details.DetailsFragment
import ru.geekbrains.myweather.viewmodel.MainViewModel
import ru.geekbrains.myweather.viewmodel.WeatherListAppState


class WeatherListFragment : Fragment(), OnItemListClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!

        }

    private val adapter = WeatherListAdapter(this)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root

    }


    var isRussian = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        switchCities()




    }


    private fun switchCities() {
        val sp = requireActivity().getSharedPreferences(KEY_SP_IS_RUSSIAN, Context.MODE_PRIVATE)
        val editor = sp.edit()
        if (sp.getBoolean(KEY_SP_IS_RUSSIAN, true)) {
            isRussian = true
            viewModel.getWeatherRussia()
        } else {
            isRussian = false
            viewModel.getWeatherWorld()
        }

        binding.floatingActionButton.setOnClickListener {

            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherRussia()
                editor.putBoolean(KEY_SP_IS_RUSSIAN, true)
                editor.apply()
            } else {
                viewModel.getWeatherWorld()
                editor.putBoolean(KEY_SP_IS_RUSSIAN, false)
                editor.apply()
            }

        }


    }


    private fun initRecycler() {
        binding.recyclerView.adapter = adapter
        val observer = object : Observer<WeatherListAppState> {
            override fun onChanged(data: WeatherListAppState) {
                renderData(data)

            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
    }


    private fun renderData(data: WeatherListAppState) {
        when (data) {
            is WeatherListAppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                mainListFragment.showSnackBar(
                    "Ошибка!",
                    "Повторить?",
                    { viewModel.getWeather(isRussian) })
            }


            is WeatherListAppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }

            is WeatherListAppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)

                if (isRussian) {

                    binding.floatingActionButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_russia
                        )
                    )
                } else {

                    binding.floatingActionButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_earth
                        )
                    )

                }
            }


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            })
        ).addToBackStack("").commit()
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


