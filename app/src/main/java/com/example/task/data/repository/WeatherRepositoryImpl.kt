package com.example.task.data.repository

import android.os.Build
import com.example.task.BuildConfig
import com.example.task.data.model.WeatherResponse
import com.example.task.domain.repository.WeatherRepository
import com.example.task.data.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepositoryImpl(private val apiService: ApiService) : WeatherRepository {

    override fun getCurrentWeather(lat: Double, lon: Double): Flow<WeatherResponse> = flow {
        try {
            val locationQuery = "$lat,$lon"
            val response = apiService.getCurrentWeather(BuildConfig.WEATHER_API_KEY, locationQuery)

            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                throw Exception("Error fetching weather data: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }.flowOn(Dispatchers.IO)
}
