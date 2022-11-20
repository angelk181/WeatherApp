package com.example.weatherapp.presentation

import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.util.Constants.EMPTY

data class WeatherState (
    val isLoading: Boolean = false,
    val results: Weather? = null,
    val error: String = EMPTY
        )