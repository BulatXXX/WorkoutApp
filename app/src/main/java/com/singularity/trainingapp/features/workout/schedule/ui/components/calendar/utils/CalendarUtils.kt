package com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.utils

import com.singularity.trainingapp.features.workout.schedule.data.ScheduleState
import java.time.LocalDate

fun getMonth(anchorMonday: LocalDate, page: Int, rows: Int): String {
    val start = pageStart(anchorMonday, page, rows).month.name.slice(0..2)
    val end = pageEnd(anchorMonday, page, rows).month.name.slice(0..2)
    return if (start == end) start
    else "$start - $end"
}

fun pageStart(baseAnchorMonday: LocalDate, page: Int, rows: Int): LocalDate {
    val delta = page - ScheduleState.BASE_PAGE
    return baseAnchorMonday.plusWeeks(delta.toLong() * rows)
}

private fun pageEnd(baseAnchorMonday: LocalDate, page: Int, rows: Int): LocalDate {
    val delta = page - ScheduleState.BASE_PAGE
    return baseAnchorMonday.plusWeeks(delta.toLong() * rows).plusDays((rows * 7 - 1).toLong())
}