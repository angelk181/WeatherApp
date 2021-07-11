package com.example.weatherapp.Response

import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 * Gets the post request from the api
 */

interface ApiService {


    // these will be wrapped around a coroutine

    @GET("weather/city")
    suspend fun getWeather():Response<Weather>



}