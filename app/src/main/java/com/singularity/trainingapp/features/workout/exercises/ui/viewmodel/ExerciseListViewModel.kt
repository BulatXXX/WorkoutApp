package com.singularity.trainingapp.features.workout.exercises.ui.viewmodel

import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.core.utils.LoadableState
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import com.singularity.trainingapp.features.workout.exercises.domain.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(private val repository: ExerciseRepository) :
    MVIViewModel<ExercisesListIntent, ExercisesListState, Nothing>() {
    init {
        val fakeExercises = List(20) { index ->
            Exercise(
                id = index.toLong(),
                name = "Упражнение #$index",
                description = if (index % 2 == 0) "Описание для упражнения #$index" else null,
                hasRepeats = index % 2 == 0,
                hasWeight = index % 3 == 0,
                hasDistance = index % 4 == 0,
                hasDuration = index % 5 == 0,
            )
        }

        // сразу в state
        setState { copy(exerciseList = fakeExercises) }

//        viewModelScope.launch {
//            repository.observeAll().collect { list ->
//                setState { uiState.value.copy(exerciseList = list) }
//            }
//        }
    }

    override fun setInitialState() = ExercisesListState()

    override fun handleIntent(intent: ExercisesListIntent) = when (intent) {
        is ExercisesListIntent.SearchExercise -> {}
    }
}

data class ExercisesListState(
    val exerciseList: List<Exercise> = emptyList(),
    val query: String = "",
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : LoadableState

sealed interface ExercisesListIntent {
    data class SearchExercise(val query: String) : ExercisesListIntent
}