package com.gb.k_1919_2.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.myweather.databinding.FragmentHistoryWeatherListBinding
import ru.geekbrains.myweather.viewmodel.HistoryViewModel
import ru.geekbrains.myweather.viewmodel.WeatherListAppState

class HistoryWeatherListFragment : Fragment() {


    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
        get() {
            return _binding!!
        }

    private val adapter = HistoryWeatherListAdapter()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    var isRussian = true
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.also {// TODO HW вынесты в initRecycler()
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        val observer = {data: WeatherListAppState -> renderData(data)}
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getAll()
    }


    private fun renderData(data: WeatherListAppState) {
        when (data) {
            is WeatherListAppState.Error -> {
                //binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is WeatherListAppState.Loading -> {
                //binding.loadingLayout.visibility = View.VISIBLE
            }
            is WeatherListAppState.Success -> {
                //binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryWeatherListFragment()
    }

}
