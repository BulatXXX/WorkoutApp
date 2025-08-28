package com.singularity.trainingapp.features.workout.schedule.ui

import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleIntent
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() :
    MVIViewModel<ScheduleIntent, ScheduleState, Nothing>() {
    override fun setInitialState() = ScheduleState()

    override fun handleIntent(intent: ScheduleIntent) = when (intent) {
        is ScheduleIntent.ChangeRows -> setState { uiState.value.copy(rows = intent.rows) }
        is ScheduleIntent.PageChanged -> setState { uiState.value.copy(currentPage = intent.page) }
        is ScheduleIntent.SelectDate -> setState { uiState.value.copy(selectedDate = intent.date) }
        is ScheduleIntent.SelectWorkout -> {}
    }
}