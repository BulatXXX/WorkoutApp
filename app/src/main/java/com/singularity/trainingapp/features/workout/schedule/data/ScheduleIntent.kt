package com.singularity.trainingapp.features.workout.schedule.data

import java.time.LocalDate

sealed interface ScheduleIntent {
    data class ChangeRows(val rows: Int) : ScheduleIntent
    data class SelectDate(val date: LocalDate) : ScheduleIntent
    data class PageChanged(val page: Int) : ScheduleIntent

    //will be implemented soon
    data class SelectWorkout(val id: String?) : ScheduleIntent
}