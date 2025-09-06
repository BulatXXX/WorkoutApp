package com.singularity.trainingapp.features.workout.schedule.domain

import com.singularity.trainingapp.features.workout.schedule.ui.DateRange
import com.singularity.trainingapp.features.workout.schedule.ui.DayMetadata
import com.singularity.trainingapp.features.workout.schedule.ui.PageSlice
import com.singularity.trainingapp.features.workout.schedule.utils.days
import java.time.LocalDate
import java.util.Locale

class BuildCalendarWindowUseCase(
    private val pagingPolicy: CalendarPagingPolicy,
    private val repository: ScheduleRepository
) {

    suspend fun execute(
        centerPage: Int,
        rows: Int,
        radius: Int
    ): Map<Int, PageSlice> {
        val pages: IntRange = pagingPolicy.pagesAround(centerPage, radius)
        val rangesByPage: Map<Int, DateRange> =
            pages.associateWith { page -> pagingPolicy.pageRange(page, rows) }

        val requiredDates: List<LocalDate> =
            rangesByPage.values.flatMap { it.days() }.distinct()

        val metadataByDate: Map<LocalDate, DayMetadata> =
            repository.getDaysMetadataByDates(requiredDates)

        return pages.associateWith { page ->
            val range = rangesByPage.getValue(page)
            buildPageSlice(range, metadataByDate)
        }
    }

    private fun buildPageSlice(
        range: DateRange,
        metadataByDate: Map<LocalDate, DayMetadata>
    ): PageSlice {
        val dotsForRange: Map<LocalDate, DayMetadata> = sliceDotsForRange(range, metadataByDate)
        return PageSlice(
            title = pagingPolicy.monthTitle(range, Locale.getDefault()),
            dates = range,
            dots = dotsForRange
        )
    }

    private fun sliceDotsForRange(
        range: DateRange,
        metadataByDate: Map<LocalDate, DayMetadata>
    ): Map<LocalDate, DayMetadata> =
        range.days().associateWith { date -> metadataByDate[date] ?: DayMetadata() }
}

