package com.singularity.trainingapp.features.workout.schedule.domain

import com.singularity.trainingapp.features.workout.schedule.ui.DateRange
import com.singularity.trainingapp.features.workout.schedule.ui.ScheduleState
import com.singularity.trainingapp.features.workout.schedule.utils.baseAnchorMonday
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import com.singularity.trainingapp.features.workout.schedule.utils.monthTitle as monthTitleUtil
import com.singularity.trainingapp.features.workout.schedule.utils.pageRange as pageRangeUtil
import com.singularity.trainingapp.features.workout.schedule.utils.pagesAround as pagesAroundUtil

interface CalendarPagingPolicy {
    val anchorMonday: LocalDate
    val basePage: Int

    fun pageRange(page: Int, rows: Int): DateRange
    fun pagesAround(centerPage: Int, radius: Int): IntRange
    fun pageForDate(date: LocalDate, rows: Int): Int
    fun monthTitle(range: DateRange, locale: Locale = Locale.getDefault()): String
    fun pageForStartMonday(startMonday: LocalDate, rows: Int): Int
}


class DefaultCalendarPagingPolicy() : CalendarPagingPolicy {

    override val basePage: Int = ScheduleState.BASE_PAGE
    override val anchorMonday: LocalDate = baseAnchorMonday()

    override fun pageRange(page: Int, rows: Int): DateRange =
        pageRangeUtil(page, rows, anchorMonday, basePage)

    override fun pagesAround(centerPage: Int, radius: Int): IntRange =
        pagesAroundUtil(centerPage, radius)

    override fun pageForDate(date: LocalDate, rows: Int): Int {
        val mondayOfDate = date.with(DayOfWeek.MONDAY)
        val weeksBetween = ChronoUnit.WEEKS.between(anchorMonday, mondayOfDate).toInt()
        val block = Math.floorDiv(weeksBetween, rows)
        return basePage + block
    }

    override fun monthTitle(range: DateRange, locale: Locale): String =
        monthTitleUtil(range, locale)

    override fun pageForStartMonday(startMonday: LocalDate, rows: Int): Int {
        val offset = ChronoUnit.WEEKS.between(anchorMonday, startMonday).toInt()
        val pageDelta = offset.floorDiv(rows)
        return basePage + pageDelta
    }
}