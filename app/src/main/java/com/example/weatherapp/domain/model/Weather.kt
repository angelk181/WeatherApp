package com.example.weatherapp.domain.model


data class Weather(
    val description: String,
    val forecast: List<Forecast>,
    val temperature: String,
    val wind: String,
    )