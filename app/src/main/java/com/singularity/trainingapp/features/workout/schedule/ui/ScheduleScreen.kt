package com.singularity.trainingapp.features.workout.schedule.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularity.trainingapp.core.ui.scaffold.LocalScaffoldStateController
import com.singularity.trainingapp.core.ui.scaffold.TopBarConfig
import com.singularity.trainingapp.core.ui.scaffold.TopBarStyle
import com.singularity.trainingapp.features.workout.schedule.ui.components.WorkoutCalendar

@Composable
fun WorkoutScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel,
    onClick: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val controller = LocalScaffoldStateController.current
    LaunchedEffect("List Screen") {
        controller.updateScaffold(TopBarConfig(title = "Calendar", style = TopBarStyle.Small))

    }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        WorkoutCalendar(state = state) { intent ->
            viewModel.sendIntent(intent)
        }
        Text("Current page: ${state.currentPage}")
        Text("Rows: ${state.rows}")
        Text("Selected: ${state.selectedDate}")
        Text("Window size: ${state.window.size}")
        Button(onClick = { onClick() }) {
            Text("Открыть упражнения")
        }
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

