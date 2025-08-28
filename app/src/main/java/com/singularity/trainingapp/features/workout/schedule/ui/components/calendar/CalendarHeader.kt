package com.singularity.trainingapp.features.workout.schedule.ui.components.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalendarHeader() {
    LazyRow {
        items(7) { index ->
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
            ) {
                Text(days[index])
            }
        }
    }
}