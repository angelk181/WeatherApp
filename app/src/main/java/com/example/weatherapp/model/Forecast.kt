package com.example.weatherapp.model

/**
 * Forecast data class, which will then be put into an array
 * in the weather data class.
 */
data class Forecast(
    val day: Int,
    val temperature: String,
    val wind: String
)