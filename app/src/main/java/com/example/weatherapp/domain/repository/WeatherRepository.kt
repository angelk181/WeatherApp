package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.util.ViewState
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(city: String): Flow<ViewState<Weather>>
}