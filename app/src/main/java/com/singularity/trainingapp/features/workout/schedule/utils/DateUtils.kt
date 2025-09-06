// features/workout/schedule/ui/components/calendar/utils/DateUtils.kt
package com.singularity.trainingapp.features.workout.schedule.utils

import com.singularity.trainingapp.features.workout.schedule.ui.DateRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

fun baseAnchorMonday(today: LocalDate = LocalDate.now()): LocalDate =
    today.with(DayOfWeek.MONDAY)

fun pageRange(
    page: Int,
    rows: Int,
    anchorMonday: LocalDate,
    basePage: Int
): DateRange {
    val delta = page - basePage
    val startDate = anchorMonday.plusWeeks(delta.toLong() * rows)
    val endDate = startDate.plusDays((rows * 7L) - 1L)
    return DateRange(startDate, endDate)
}

fun pagesAround(centerPage: Int, radius: Int = 1): IntRange =
    (centerPage - radius)..(centerPage + radius)

fun monthTitle(range: DateRange, locale: Locale = Locale.getDefault()): String {
    val first = range.startDate
    val last = range.endDate
    fun capitalize(s: String) =
        s.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

    val startMonth = first.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
    val endMonth = last.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
    return if (first.month == last.month) capitalize(startMonth)
    else "${capitalize(startMonth)}–${capitalize(endMonth)}"
}

fun localizedWeekdays(
    locale: Locale = Locale.getDefault(),
    start: DayOfWeek = DayOfWeek.MONDAY
): List<String> {
    val days = DayOfWeek.entries
    val shifted = days.drop(start.ordinal) + days.take(start.ordinal)
    return shifted.map { it.getDisplayName(TextStyle.SHORT, locale) }
}

val DateRange.daysCount: Int
    get() = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

fun DateRange.days(): List<LocalDate> =
    List(daysCount) { i -> startDate.plusDays(i.toLong()) }