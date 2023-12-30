package com.example.task.ui.comman

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.task.ui.screens.weather.WeatherViewModel
import com.example.task.ui.screens.weather.getCurrentLocation

@Composable
fun GpsEnableDialog(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    var isGpsEnabled by remember { mutableStateOf(isGpsEnabled(context)) }
    var showGpsDialog by remember { mutableStateOf(!isGpsEnabled) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleObserver = rememberUpdatedState(newValue = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            isGpsEnabled = isGpsEnabled(context)
            showGpsDialog = !isGpsEnabled
        }
    })
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = lifecycleObserver.value
        val owner = lifecycleOwner.lifecycle
        owner.addObserver(observer)
        onDispose {
            owner.removeObserver(observer)
        }
    })
    LaunchedEffect(Unit) {
        isGpsEnabled = isGpsEnabled(context)
        showGpsDialog = !isGpsEnabled
    }

    if (showGpsDialog) {
        AlertDialog(
            onDismissRequest = { showGpsDialog = false },
            title = { Text("Enable GPS") },
            text = { Text("GPS is required for this app to function. Please enable GPS.") },
            confirmButton = {
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    showGpsDialog = false
                }) {
                    Text("Open Settings")
                }
            }
        )
    } else {
        getCurrentLocation(context) { loc ->
            viewModel.updateLocation(loc)
            viewModel.loadCurrentWeather(loc.latitude, loc.longitude)
        }
    }
}

fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}
