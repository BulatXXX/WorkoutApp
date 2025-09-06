package com.singularity.trainingapp.features.workout.schedule.ui

import androidx.compose.runtime.Immutable
import com.singularity.trainingapp.core.utils.LoadableState
import java.time.LocalDate

@Immutable
data class Dot(val type: String, val color: Long)

@Immutable
data class DayMetadata(val dots: List<Dot> = emptyList())

@Immutable
data class PageSlice(
    val title: String,
    val dates: DateRange,
    val dots: Map<LocalDate, DayMetadata>,
)

@Immutable
data class DateRange(
    val startDate: LocalDate,
    val endDate: LocalDate,
)

@Immutable
data class ScheduleState(
    val rows: Int = 2,
    val currentPage: Int = BASE_PAGE,
    val selectedDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now(),
    val window: Map<Int, PageSlice> = emptyMap(),
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : LoadableState {
    companion object {
        const val BASE_PAGE = 10_000
        const val PAGE_COUNT = BASE_PAGE * 2
    }
}