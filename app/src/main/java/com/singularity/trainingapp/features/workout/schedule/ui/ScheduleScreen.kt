package com.singularity.trainingapp.features.workout.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.WorkoutCalendar

@Composable
fun WorkoutScheduleScreen(modifier: Modifier = Modifier, viewModel: ScheduleViewModel) {

    val state by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        WorkoutCalendar(state = state) { intent ->
            viewModel.sendIntent(intent)
        }
        WorkoutDayDetails()
    }
}

@Composable
fun WorkoutDayDetails(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Red)
            .fillMaxWidth()
            .height(400.dp)
    )
}

