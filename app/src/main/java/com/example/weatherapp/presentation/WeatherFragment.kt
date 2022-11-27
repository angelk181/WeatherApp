package com.example.weatherapp.presentation

import android.location.Address
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import com.example.weatherapp.util.DeviceLocationTracker
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment(), DeviceLocationTracker.DeviceLocationListener  {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WeatherViewModel>()

    private lateinit var deviceLocationTracker: DeviceLocationTracker

    // weather animation object to initialize
    private lateinit var weatherAnimation: PrecipType

    private val pattern1 = Regex("rain")
    private val pattern2 = Regex("snow")
    private val pattern3 = Regex("Sunny")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weather.collect { state ->

                    when (state) {
                        WeatherState(isLoading = state.isLoading) -> {
                        }
                        WeatherState(error = state.error) -> {
                            Snackbar.make(
                                binding.clWeatherHome,
                                "Oh no! " + state.error,
                                Snackbar.LENGTH_INDEFINITE
                            ).show()
                        }
                        WeatherState(results = state.results) -> {
                            binding.apply {

                                tvTemperature.text = state.results?.temperature
                                tvDescription.text = state.results?.description
                                tvWind.text = state.results?.wind


                                tvForecast1.text =
                                    "${state.results?.forecast?.get(0)?.temperature}/${
                                        state.results?.forecast?.get(0)?.wind
                                    }"
                                tvForecast2.text =
                                    "${state.results?.forecast?.get(1)?.temperature}/${
                                        state.results?.forecast?.get(1)?.wind
                                    }"
                                tvForecast3.text =
                                    "${state.results?.forecast?.get(2)?.temperature}/${
                                        state.results?.forecast?.get(2)?.wind
                                    }"


                                val rainResult =
                                    state.results?.description?.let { pattern1.containsMatchIn(it) }
                                val snowResult =
                                    state.results?.description?.let { pattern2.containsMatchIn(it) }
                                val sunnyResult =
                                    state.results?.description?.let { pattern3.containsMatchIn(it) }


                                weatherAnimation = when {

                                    rainResult == true -> {
                                        PrecipType.RAIN
                                    }

                                    snowResult == true -> {
                                        PrecipType.SNOW
                                    }
                                    else -> {
                                        PrecipType.CLEAR
                                    }
                                }

                                weatherView.setWeatherData(weatherAnimation)

                                when {
                                    rainResult == true -> {
                                        weatherImage.setBackgroundResource(R.mipmap.ic_rain)
                                        clWeatherHome.setBackgroundResource(R.drawable.background2)

                                    }
                                    sunnyResult == true -> {
                                        weatherImage.setBackgroundResource(R.mipmap.ic_sun)
                                        clWeatherHome.setBackgroundResource(R.drawable.background3)
                                    }
                                    snowResult == true -> {
                                        weatherImage.setBackgroundResource(R.mipmap.ic_snow)
                                        clWeatherHome.setBackgroundResource(R.drawable.background2)
                                    }
                                    else -> {
                                        weatherImage.setBackgroundResource(R.mipmap.ic_cloud)
                                        clWeatherHome.setBackgroundResource(R.drawable.background)
                                    }
                                }


                            }
                        }


                    }
                }
            }
        }

        viewModel.forecastDays.observe(viewLifecycleOwner)
        {
            binding.tvDay1.text = it.dayOne
            binding.tvDay2.text = it.dayTwo
            binding.tvDay3.text = it.dayThree
        }

        viewModel.setForecastDays()


        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceLocationTracker = DeviceLocationTracker(this.requireContext(), this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentWeatherBinding.inflate(inflater, container, false )
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onDeviceLocationChanged(results: List<Address>?) {
        val currentLocation = results?.get(0)
        currentLocation?.apply {
            val cityName = "London"

            Log.d("City", cityName)
            // for Ui threading
            GlobalScope.launch(Dispatchers.Main) {

                viewModel.getWeather(currentLocation.subAdminArea)
                binding.tvCity.text = currentLocation.subAdminArea

            }
        }
    }

}