package com.example.weatherapp.Response

import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("weather/{city}")
    suspend fun getWeather(
    @Path("city") city: String):Response<Weather>

}