package com.example.weatherapp.repository

import com.example.weatherapp.Response.ApiService
import javax.inject.Inject

class WeatherRepository
@Inject
constructor(private  val apiService: ApiService) {

    suspend fun getWeather(city: String) = apiService.getWeather(city)

}

