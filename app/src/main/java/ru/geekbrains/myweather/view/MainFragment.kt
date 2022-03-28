package ru.geekbrains.myweather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.myweather.databinding.FragmentMainBinding
import ru.geekbrains.myweather.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null // утечка памяти//UPD: Разобрался. Но есть еще одно решение с перемещением области видимости из фрагмента в область функции.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View /*? теперь вью точно не ноль */ {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = object : Observer<Any> {
            override fun onChanged(data: Any) {
                renderData(data)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    private fun renderData(data: Any) {
        Toast.makeText(requireContext(), "Работает", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
