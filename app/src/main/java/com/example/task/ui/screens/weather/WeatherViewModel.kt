package com.example.task.ui.screens.weather

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.model.WeatherResponse
import com.example.task.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    var location = mutableStateOf<Location?>(null)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun updateLocation(newLocation: Location) {
        location.value = newLocation
    }

    fun loadCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getCurrentWeather(lat, lon).collect { data ->
                _weatherData.value = data
                _isRefreshing.value = false

            }
        }
    }
}
