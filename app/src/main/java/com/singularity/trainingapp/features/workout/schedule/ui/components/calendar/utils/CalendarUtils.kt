package com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.utils

import com.singularity.trainingapp.features.workout.schedule.data.ScheduleState
import java.time.LocalDate

private const val BASE_PAGE = ScheduleState.BASE_PAGE

fun pageStart(anchorMonday: LocalDate, page: Int, rows: Int): LocalDate {
    val delta = page - BASE_PAGE
    return anchorMonday.plusWeeks(delta.toLong() * rows)
}

