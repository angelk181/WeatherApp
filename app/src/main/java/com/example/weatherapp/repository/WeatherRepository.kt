package com.example.weatherapp.repository

import com.example.weatherapp.Response.ApiService
import javax.inject.Inject

/**
 * communication between ApiService and viewModel
 */
class WeatherRepository
@Inject
constructor(private  val apiService: ApiService) {

    suspend fun getWeather() = apiService.getWeather()

}

