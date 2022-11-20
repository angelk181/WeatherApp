package com.example.weatherapp.data.remote.repository

import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.util.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
): WeatherRepository {

    override fun getWeather(city: String): Flow<ViewState<Weather>> {
        return flow {
            try {
                emit(ViewState.Loading<Weather>())
                val weather = weatherApi.getWeather(city)
                emit(
                    ViewState.Success<Weather>(
                        weather
                    )
                )
            } catch (e: HttpException) {
                emit(
                    ViewState.Error<Weather>(
                        e.localizedMessage ?: "An unexpected error has occurred"
                    )
                )
            } catch (e: IOException) {
                emit(
                    ViewState.Error<Weather>(
                        "Couldn't reach server. Please check internet connection"
                    )
                )

            }
        }.flowOn(Dispatchers.IO)
    }
}