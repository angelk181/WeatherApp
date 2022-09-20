package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Util.Resource
import com.example.weatherapp.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(private val repository: WeatherRepository) : ViewModel() {


    private val _weather = MutableLiveData<Resource<Weather>>()
    val weather = _weather as LiveData<Resource<Weather>>



    val dayOfWeek: DayOfWeek = LocalDate.now().dayOfWeek
    val dayNum: Int = dayOfWeek.value

    val daysMap = mapOf(
        0 to "Sunday",
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday"
    )

    val day1: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val day2: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val day3: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }





    fun getWeather(city: String) = viewModelScope.launch {
        repository.getWeather(city).let { response ->
            if (response.isSuccessful) {
                _weather.postValue(response.body()?.let { weather ->  Resource.Success(weather.copy()) })
            } else{
               Log.d("Tag","getWeather Error Response: ${response.message()}")
            }
        }
    }

}