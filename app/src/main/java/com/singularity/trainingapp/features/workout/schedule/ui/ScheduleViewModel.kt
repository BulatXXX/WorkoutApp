package com.singularity.trainingapp.features.workout.schedule.ui

import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.features.workout.schedule.data.DayMetadata
import com.singularity.trainingapp.features.workout.schedule.data.DaysRange
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleIntent
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleState
import com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.computePageRange
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() :
    MVIViewModel<ScheduleIntent, ScheduleState, Nothing>() {
    override fun setInitialState(): ScheduleState {
        val base = ScheduleState.BASE_PAGE
        val rows = 2
        val anchorMonday = LocalDate.now().with(DayOfWeek.MONDAY)
        val ranges = listOf(
            computePageRange(anchorMonday, base - 1, rows),
            computePageRange(anchorMonday, base, rows),
            computePageRange(anchorMonday, base + 1, rows),
        )

        // заготовим пустые корзины
        val windowInit: Map<DaysRange, Map<LocalDate, DayMetadata>> =
            ranges.associateWith { emptyMap() }

        return ScheduleState(
            rows = rows,
            selectedDate = LocalDate.now(),
            today = LocalDate.now(),
            currentPage = base,
            window = windowInit
        )
    }


    override fun handleIntent(intent: ScheduleIntent) = when (intent) {
        is ScheduleIntent.ChangeRows -> setState { uiState.value.copy(rows = intent.rows) }
        is ScheduleIntent.PageChanged -> setState { uiState.value.copy(currentPage = intent.page) }
        is ScheduleIntent.SelectDate -> setState { uiState.value.copy(selectedDate = intent.date) }
        is ScheduleIntent.SelectWorkout -> {}
    }
}