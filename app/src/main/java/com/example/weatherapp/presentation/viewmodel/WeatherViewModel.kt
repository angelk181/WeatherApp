package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.util.ViewState
import com.example.weatherapp.domain.model.ForecastDayModel
import com.example.weatherapp.domain.usecase.WeatherUseCase
import com.example.weatherapp.presentation.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(private val weatherUseCase: WeatherUseCase,
) : ViewModel() {


    private val _weather = MutableStateFlow(WeatherState())
    val weather = _weather.asStateFlow()

    private var _forecastDays = MutableLiveData<ForecastDayModel>()
    val forecastDays = _forecastDays as LiveData<ForecastDayModel>



    private val dayOfWeek: DayOfWeek = LocalDate.now().dayOfWeek
    private val dayNum: Int = dayOfWeek.value

    private val daysMap = mapOf(
        0 to "Sunday",
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday"
    )

    fun setForecastDays() {
        _forecastDays.value = ForecastDayModel(
            daysMap[(dayNum + 1) % 7],
            daysMap[(dayNum + 2) % 7],
            daysMap[(dayNum + 3) % 7]
        )
    }


    fun getWeather(city: String) {
        weatherUseCase(city).onEach {
            result ->
            when (result) {
                is ViewState.Loading -> {
                    _weather.value = WeatherState(isLoading = true)
                }
                is ViewState.Success -> {
                    _weather.value = WeatherState(results = result.data)
                }
                is ViewState.Error -> {
                    _weather.value = WeatherState(error = result.message ?: "Unexpected error occurred")

                }

            }
        }.launchIn(viewModelScope)
    }

}