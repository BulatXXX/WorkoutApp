package com.singularity.trainingapp.features.workout.schedule.data

import androidx.compose.runtime.Immutable
import com.singularity.trainingapp.core.utils.LoadableState
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Dot(val type: String, val color: Long) // только тип/цвет

data class DaysRange(
    val start: LocalDate,
    val endExclusive: LocalDate,
) {
    val daysCount: Int get() = ChronoUnit.DAYS.between(start, endExclusive).toInt()
}

fun DaysRange.days(): List<LocalDate> = List(daysCount) { index -> start.plusDays(index.toLong()) }


data class ScheduleState(
    val rows: Int = 2,
    val selectedDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now(),
    val currentPage: Int = BASE_PAGE,

    val window: Map<DaysRange, Map<LocalDate, DayMetadata>> = emptyMap(),
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : LoadableState {
    companion object {
        const val BASE_PAGE = 10_000
        const val PAGE_COUNT = BASE_PAGE * 2
    }
}

@Immutable
data class DayMetadata(val dots: List<Dot> = emptyList())

@Immutable
data class PageUi(
    val title: String,                  // "Сентябрь" или "Сентябрь–Октябрь"
    val dates: List<LocalDate>,         // список дат для текущей страницы
    val dots: Map<LocalDate, DayMetadata>,
    val selectedDate: LocalDate,
    val today: LocalDate,
)