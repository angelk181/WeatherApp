package com.example.weatherapp.Response

import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 * Gets the post request from the api
 * interface as retrofit has been set to generate the implementation
 * dagger will inject api where it's needed
 */

interface ApiService {


    // A method that gets response as an async. these will be wrapped around a coroutine

    @GET("weather/city")
    suspend fun getWeather():Response<Weather>



}