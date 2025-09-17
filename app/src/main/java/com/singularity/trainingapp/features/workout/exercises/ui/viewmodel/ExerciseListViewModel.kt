package com.singularity.trainingapp.features.workout.exercises.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.core.utils.LoadableState
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import com.singularity.trainingapp.features.workout.exercises.domain.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(private val repository: ExerciseRepository) :
    MVIViewModel<ExercisesListIntent, ExercisesListState, Nothing>() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeAll().collect { list ->
                setState { uiState.value.copy(exerciseList = list) }
            }
        }
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