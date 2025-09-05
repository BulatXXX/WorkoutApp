package com.singularity.trainingapp.features.workout.schedule.domain

import com.singularity.trainingapp.features.workout.schedule.ui.state.DayMetadata
import java.time.LocalDate

interface ScheduleRepository {
    suspend fun getDaysMetadataByDates(dates: List<LocalDate>): Map<LocalDate, DayMetadata>
}