package com.example.weatherapp.di

import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.data.remote.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.util.Constants.Base_Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    
    @Provides
    @Singleton
    fun providesRepository(weatherApi: WeatherApi): WeatherRepository = WeatherRepositoryImpl(weatherApi)

    @Provides
    @Singleton
    fun provideRetrofitInstance(): WeatherApi =
        Retrofit.Builder()
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)


}