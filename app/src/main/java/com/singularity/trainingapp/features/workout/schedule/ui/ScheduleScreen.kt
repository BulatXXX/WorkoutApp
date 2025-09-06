package com.singularity.trainingapp.features.workout.schedule.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singularity.trainingapp.features.workout.schedule.ui.components.WorkoutCalendar

@Composable
fun WorkoutScheduleScreen(modifier: Modifier = Modifier, viewModel: ScheduleViewModel) {

    val state by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        WorkoutCalendar(state = state) { intent ->
            viewModel.sendIntent(intent)
        }
        Text("Current page: ${state.currentPage}")
        Text("Rows: ${state.rows}")
        Text("Selected: ${state.selectedDate}")
        Text("Window size: ${state.window.size}")
    }
}

//@Composable
//fun WorkoutDayDetails(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .background(Color.Red)
//            .fillMaxWidth()
//            .height(400.dp)
//    )
//}

