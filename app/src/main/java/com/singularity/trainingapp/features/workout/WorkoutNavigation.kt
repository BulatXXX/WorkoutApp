package com.singularity.trainingapp.features.workout

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.singularity.trainingapp.core.navigation.ScheduleRoute
import com.singularity.trainingapp.core.navigation.TabWorkout
import com.singularity.trainingapp.core.navigation.WorkoutDetail
import com.singularity.trainingapp.core.ui.TestScreen
import com.singularity.trainingapp.features.workout.schedule.ui.WorkoutScheduleScreen

fun NavGraphBuilder.workoutGraph(navController: NavController){
    navigation<TabWorkout>(startDestination = ScheduleRoute) {
        composable<WorkoutDetail> { backstack ->
            val args = backstack.toRoute<WorkoutDetail>()
            TestScreen("WorkoutName: ${args.id}")
        }

        composable<ScheduleRoute> {
            WorkoutScheduleScreen(
                viewModel = hiltViewModel()
            )
        }
    }
}