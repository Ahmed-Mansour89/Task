package com.example.task.ui.compenents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.task.data.model.WeatherResponse

@Composable
fun WeatherItem(forecastDay: WeatherResponse.Forecast.Forecastday) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "https:${forecastDay.day?.condition?.icon}"),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(text = forecastDay.date ?: "", style = MaterialTheme.typography.h6)
                Text(
                    text = forecastDay.day?.condition?.text ?: "",
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Text(
                text = "${forecastDay.day?.avgtempC}°C",
                style = MaterialTheme.typography.h5
            )
        }
    }
}
