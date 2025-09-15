package com.singularity.trainingapp.features.workout.exercises.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.core.utils.LoadableState
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import com.singularity.trainingapp.features.workout.exercises.domain.ExerciseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExerciseCreateViewModel @Inject constructor(private val exerciseRepository: ExerciseRepository) :
    MVIViewModel<ExerciseCreateIntent, ExerciseCreateState, Nothing>() {
    override fun setInitialState(): ExerciseCreateState = ExerciseCreateState()

    override fun handleIntent(intent: ExerciseCreateIntent) = when (intent) {
        is ExerciseCreateIntent.DescriptionChanged -> setState {
            uiState.value.copy(
                exerciseDescription = intent.description
            )
        }

        is ExerciseCreateIntent.NameChanged -> setState {
            uiState.value.copy(exerciseName = intent.name)
        }

        ExerciseCreateIntent.SaveExercise -> {
            val nothing = uiState.value.apply {
                if (exerciseName.isEmpty()) {
                    setState { copy(error = "Exercise name is empty") }
                    return@apply
                }
                setState { copy(isLoading = true) }
                val exercise = Exercise(
                    name = exerciseName,
                    description = exerciseDescription,
                    hasRepeats = hasRepeats,
                    hasDistance = hasDistance,
                    hasWeight = hasWeight

                )
                viewModelScope.launch {
                    exerciseRepository.upsert(exercise)
                    setState { copy(isLoading = false) }
                }
            }
        }

        ExerciseCreateIntent.ToggleHasDistance -> setState {
            val hasDistance = uiState.value.hasDistance
            uiState.value.copy(hasDistance = !hasDistance)
        }

        ExerciseCreateIntent.ToggleHasRepeats -> setState {
            val hasRepeats = uiState.value.hasRepeats
            uiState.value.copy(hasRepeats = !hasRepeats)
        }

        ExerciseCreateIntent.ToggleHasWeight -> setState {
            val hasWeight = uiState.value.hasWeight
            uiState.value.copy(hasWeight = !hasWeight)
        }
    }
}

sealed interface ExerciseCreateIntent {
    data object ToggleHasRepeats : ExerciseCreateIntent
    data object ToggleHasDistance : ExerciseCreateIntent
    data object ToggleHasWeight : ExerciseCreateIntent
    data class NameChanged(val name: String) : ExerciseCreateIntent
    data class DescriptionChanged(val description: String) : ExerciseCreateIntent
    data object SaveExercise : ExerciseCreateIntent

}

data class ExerciseCreateState(
    val exerciseName: String = "",
    val exerciseDescription: String = "",
    val hasRepeats: Boolean = false,
    val hasDistance: Boolean = false,
    val hasWeight: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : LoadableState

sealed interface ExerciseCreateEffect {
    data class ShowToast(val message: String) : ExerciseCreateEffect
}