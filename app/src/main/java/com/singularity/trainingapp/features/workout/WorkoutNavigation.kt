package com.singularity.trainingapp.features.workout

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.singularity.trainingapp.core.navigation.TabWorkout
import com.singularity.trainingapp.core.navigation.WorkoutDetail
import com.singularity.trainingapp.core.navigation.WorkoutList
import com.singularity.trainingapp.core.ui.TestScreen

fun NavGraphBuilder.workoutGraph(navController: NavController){
    navigation<TabWorkout>(startDestination = WorkoutList) {
        composable<WorkoutList> {
            WorkoutListScreen { name ->
                navController.navigate(WorkoutDetail(name))
            }
        }
        composable<WorkoutDetail> { backstack ->
            val args = backstack.toRoute<WorkoutDetail>()
            TestScreen("WorkoutName: ${args.id}")
        }
    }
}