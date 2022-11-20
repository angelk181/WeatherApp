package com.example.weatherapp.data.remote

import com.example.weatherapp.domain.model.Weather
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {

    @GET("weather/{city}")
    suspend fun getWeather(
    @Path("city") city: String): Weather

}