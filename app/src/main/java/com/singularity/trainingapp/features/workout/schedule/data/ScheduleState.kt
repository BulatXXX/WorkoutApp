package com.singularity.trainingapp.features.workout.schedule.data

import com.singularity.trainingapp.core.utils.LoadableState
import java.time.LocalDate


data class ScheduleState(
    //Calendar
    val rows: Int = 2,
    val baseAnchorMonday: LocalDate = LocalDate.now().with(java.time.DayOfWeek.MONDAY),
    val selectedDate: LocalDate = LocalDate.now(),
    val currentDate: LocalDate = LocalDate.now(),
    val currentPage: Int = BASE_PAGE,
    val daysMetadata: DaysMetadata = DaysMetadata(),
    //Details

    //LoadableState
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : LoadableState {
    companion object {
        const val BASE_PAGE = 10000
        const val PAGE_COUNT = Int.MAX_VALUE

    }
}

class DaysMetadata {

}
