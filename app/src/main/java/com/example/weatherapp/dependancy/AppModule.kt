package com.example.weatherapp.dependancy

import com.example.weatherapp.Response.ApiService
import com.example.weatherapp.Util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * we want to provide
 * context needed and dagger knows context
 * singleton makes sure it takes the same instance over again
 **/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.Base_Url

    @Provides
    @Singleton
    fun provideRetrofitInstance(Base_URL: String) : ApiService =
        Retrofit.Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)


}