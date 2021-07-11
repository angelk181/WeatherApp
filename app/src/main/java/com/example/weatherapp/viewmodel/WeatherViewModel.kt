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
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(private val repository: WeatherRepository) : ViewModel() {


    val _response = MutableLiveData<Weather>()
    val apiResponse: MutableLiveData<Resource<Weather>> = MutableLiveData()
    val weatherResponse: LiveData<Weather>
    get() = _response

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