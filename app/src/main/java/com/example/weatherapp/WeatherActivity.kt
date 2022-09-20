package com.example.weatherapp

import android.location.Address
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.weatherapp.Util.DeviceLocationTracker
import com.example.weatherapp.Util.Resource
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class WeatherActivity() : AppCompatActivity(), DeviceLocationTracker.DeviceLocationListener {

    //calls view binding object
    private lateinit var binding: ActivityMainBinding

    //view Model object
    private val weatherViewModel: WeatherViewModel by viewModels()

    // weather animation object to initialize
    private lateinit var weatherAnimation: PrecipType

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var deviceLocationTracker: DeviceLocationTracker


    // Regex for weather description from Api Service
    private val pattern1 = Regex("rain")
    private val pattern2 = Regex("snow")
    private val pattern3 = Regex("Sunny")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialises view binding for main activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        deviceLocationTracker = DeviceLocationTracker(this, this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /**
         * observes weather response and applies data
         * from repository to the views
         */
        weatherViewModel.weather.observe(this, { weather ->



            binding.apply {


                tvTemperature.text = weather.data?.temperature
                tvDescription.text = weather.data?.description
                tvWind.text = weather.data?.wind


                val forecast1 = weather.data?.forecast?.get(0)
                val forecast2 = weather.data?.forecast?.get(1)
                val forecast3 = weather.data?.forecast?.get(2)

                tvForecast1.text = "${forecast1?.temperature}/${forecast2?.wind}"
                tvForecast2.text = "${forecast2?.temperature}/${forecast2?.wind}"
                tvForecast3.text = "${forecast3?.temperature}/${forecast2?.wind}"


                val rainResult = weather?.data?.description?.let { pattern1.containsMatchIn(it) }
                val snowResult = weather.data?.description?.let { pattern2.containsMatchIn(it) }
                val sunnyResult = weather?.data?.description?.let { pattern3.containsMatchIn(it) }


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
                        window.statusBarColor =
                            ContextCompat.getColor(this@WeatherActivity, R.color.grey2)
                        return@apply scrollView.setBackgroundResource(R.drawable.background2)
                    }
                    sunnyResult == true -> {
                        weatherImage.setBackgroundResource(R.mipmap.ic_sun)
                        window.statusBarColor =
                            ContextCompat.getColor(this@WeatherActivity, R.color.red)
                        return@apply scrollView.setBackgroundResource(R.drawable.background3)
                    }
                    snowResult == true -> {
                        weatherImage.setBackgroundResource(R.mipmap.ic_snow)
                        window.statusBarColor =
                            ContextCompat.getColor(this@WeatherActivity, R.color.grey2)
                        return@apply scrollView.setBackgroundResource(R.drawable.background2)
                    }
                    else -> {
                        weatherImage.setBackgroundResource(R.mipmap.ic_cloud)
                        window.statusBarColor =
                            ContextCompat.getColor(
                                this@WeatherActivity,
                                R.color.colorPrimary
                            )
                        return@apply scrollView.setBackgroundResource(R.drawable.background)
                    }
                }


            }

        })


        weatherViewModel.weather.observe(this, { weather ->
            when (weather) {
                is Resource.Error -> {
                    Snackbar.make(
                        binding.scrollView,
                        "Oh no! Theres a network error, the weather will show soon. Please restart the app another time :)",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
                else -> {

                }
            }
        }
        )

        weatherViewModel.day1.observe(this, {
            binding.tvDay1.text = it

        }
        )
        weatherViewModel.day2.observe(this,) {
            binding.tvDay2.text = it
        }
        weatherViewModel.day3.observe(this, {
            binding.tvDay3.text = it
        }
        )

        forecastDays()

    }

    private fun forecastDays() {

        weatherViewModel.day1.value = weatherViewModel.daysMap[(weatherViewModel.dayNum + 1) % 7]
        weatherViewModel.day2.value = weatherViewModel.daysMap[(weatherViewModel.dayNum + 2) % 7]
        weatherViewModel.day3.value = weatherViewModel.daysMap[(weatherViewModel.dayNum + 3) % 7]

    }


    override fun onDeviceLocationChanged(results: List<Address>?) {
        val currntLocation = results?.get(0)
        currntLocation?.apply {
            val cityName = "London"

            Log.d("City",cityName)
            // for Ui threading
            GlobalScope.launch(Dispatchers.Main) {

                weatherViewModel.getWeather(cityName)
                binding.tvCity.text = cityName


            }


        }
    }
}






