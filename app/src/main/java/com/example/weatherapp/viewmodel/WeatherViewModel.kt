package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Util.ViewState
import com.example.weatherapp.model.ForecastDayModel
import com.example.weatherapp.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(private val repository: WeatherRepository) : ViewModel() {


    private val _weather = MutableLiveData<ViewState<Weather>>()
    val weather = _weather as LiveData<ViewState<Weather>>

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


    fun getWeather(city: String) = viewModelScope.launch {
        repository.getWeather(city).let { response ->
            if (response.isSuccessful) {
                _weather.value = response.body()?.copy()?.let { ViewState.Success(it) }

            } else{
               Log.d("Tag","getWeather Error Response: ${response.message()}")
                _weather.value = ViewState.Error(response.message())
            }
        }
    }

}