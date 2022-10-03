package com.example.weatherapp.di

import com.example.weatherapp.Response.ApiService
import com.example.weatherapp.Util.Constants
import com.example.weatherapp.Util.Constants.Base_Url
import com.example.weatherapp.repository.WeatherRepository
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
    fun providesRepository(apiService: ApiService) = WeatherRepository(apiService)

    @Provides
    @Singleton
    fun provideRetrofitInstance(): ApiService =
        Retrofit.Builder()
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)


}