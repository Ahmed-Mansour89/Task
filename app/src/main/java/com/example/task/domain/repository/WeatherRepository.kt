package com.example.task.domain.repository

import com.example.task.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(lat: Double, lon: Double): Flow<WeatherResponse>
}
