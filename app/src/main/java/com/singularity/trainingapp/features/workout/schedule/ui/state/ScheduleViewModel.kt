package com.singularity.trainingapp.features.workout.schedule.ui.state

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.features.workout.schedule.domain.BuildCalendarWindowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val buildCalendarWindow: BuildCalendarWindowUseCase,
) :
    MVIViewModel<ScheduleIntent, ScheduleState, Nothing>() {

    private var buildWindowJob: Job? = null

    override fun setInitialState(): ScheduleState {
        val initialState = ScheduleState(isLoading = true)
        buildWindowFor(centerPage = initialState.currentPage, rows = initialState.rows)
        return initialState
    }

    override fun handleIntent(intent: ScheduleIntent) = when (intent) {
        is ScheduleIntent.ChangeRows -> {
            setState { uiState.value.copy(rows = intent.rows) }
            buildWindowFor(centerPage = uiState.value.currentPage, rows = intent.rows)
        }

        is ScheduleIntent.PageChanged -> {
            setState { uiState.value.copy(currentPage = intent.page) }
            buildWindowFor(centerPage = intent.page, rows = uiState.value.rows)
        }

        is ScheduleIntent.SelectDate -> setState { uiState.value.copy(selectedDate = intent.date) }
        is ScheduleIntent.SelectWorkout -> {}
    }

    private fun buildWindowFor(centerPage: Int, rows: Int) {
        buildWindowJob?.cancel()
        buildWindowJob = viewModelScope.launch {
            runCatching {
                val window = buildCalendarWindow.execute(
                    centerPage = centerPage,
                    rows = rows,
                    radius = 1
                )
                val stillRelevant = uiState.value.currentPage == centerPage &&
                        uiState.value.rows == rows
                if (stillRelevant) {
                    setState {
                        uiState.value.copy(
                            window = window,
                            error = null,
                            isLoading = false
                        )
                    }
                }
            }.onFailure {
                setState { uiState.value.copy(error = it.message ?: "Unknown error") }
                Log.e("ScheduleViewModel", it.message ?: "Unknown error")
            }

        }
    }
}

