package com.example.task.ui.screens.weather

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.task.ui.comman.GpsEnableDialog
import com.example.task.ui.compenents.WeatherItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.Module

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeatherScreen() {
    val viewModel = hiltViewModel<WeatherViewModel>()
    val weatherData by viewModel.weatherData.collectAsState()
    val isRefreshing by remember { mutableStateOf(false) }

    GpsEnableDialog(viewModel)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather Forecast") })
        }
    ) {


        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing),
            onRefresh = {
                viewModel.location.value?.let {
                    viewModel.loadCurrentWeather(it.latitude, it.longitude)
                }
            }
        ) {
            LazyColumn (modifier = Modifier.fillMaxSize().padding(20.dp)){
                item {
                    viewModel.location.let { loc ->
                        Text(text = "Latitude: ${loc.value?.latitude}, Longitude: ${loc.value?.longitude}")
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(weatherData?.forecast?.forecastday ?: emptyList()) { day ->
                    day.let { WeatherItem(it!!) }
                }
            }
        }
    }

}


fun getCurrentLocation(context: Context, onLocationReceived: (Location) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.Builder(10000L)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { onLocationReceived(it) }
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )

}

