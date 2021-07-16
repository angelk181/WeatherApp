package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Util.Resource
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


    val _response = MutableLiveData<Weather>()
    val apiResponse: MutableLiveData<Resource<Weather>> = MutableLiveData()
    val weatherResponse: LiveData<Weather>
    get() = _response

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

    init {
        getWeather()

    }


    private fun getWeather() = viewModelScope.launch {
        repository.getWeather().let { response ->
            if (response.isSuccessful) {
                _response.postValue(response.body())
                apiResponse.postValue(Resource.Success(response.body()!!))

            } else{
                apiResponse.postValue(Resource.Error(response.message()))
               Log.d("Tag","getWeather Error Response: ${response.message()}")
            }
        }
    }

}