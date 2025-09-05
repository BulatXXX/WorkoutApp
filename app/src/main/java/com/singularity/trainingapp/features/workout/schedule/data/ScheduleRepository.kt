package com.singularity.trainingapp.features.workout.schedule.data

import com.singularity.trainingapp.features.workout.schedule.domain.ScheduleRepository
import com.singularity.trainingapp.features.workout.schedule.ui.state.DayMetadata
import com.singularity.trainingapp.features.workout.schedule.ui.state.Dot
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject


class ScheduleRepositoryImpl @Inject constructor() : ScheduleRepository {
    override suspend fun getDaysMetadataByDates(
        dates: List<LocalDate>
    ): Map<LocalDate, DayMetadata> {
        return dates.associateWith { date -> fakeMetaFor(date) }
    }

    private fun fakeMetaFor(date: LocalDate): DayMetadata = when {
        date.year == 2025 && date.month == Month.JUNE -> juneMeta(date)
        date.year == 2025 && date.month == Month.AUGUST -> augustMeta(date)
        else -> DayMetadata(emptyList())
    }

    private fun juneMeta(date: LocalDate): DayMetadata {
        val dots = mutableListOf<Dot>()
        if (date.dayOfMonth % 2 == 0) dots += Dot(type = "strength", color = 0xFF1565C0) // синий
        if (date.dayOfMonth % 3 == 0) dots += Dot(type = "cardio", color = 0xFFE53935) // красный
        if (date.dayOfWeek.value >= DayOfWeek.SATURDAY.value) dots += Dot(
            type = "mobility",
            color = 0xFF6D4C41
        ) // коричневый
        return DayMetadata(dots)
    }

    private fun augustMeta(date: LocalDate): DayMetadata {
        val dots = mutableListOf<Dot>()
        if (date.dayOfWeek == DayOfWeek.MONDAY) dots += Dot(
            type = "legs",
            color = 0xFF2E7D32
        )  // зелёный
        if (date.dayOfWeek == DayOfWeek.WEDNESDAY) dots += Dot(
            type = "push",
            color = 0xFF8E24AA
        )  // фиолетовый
        if (date.dayOfWeek == DayOfWeek.FRIDAY) dots += Dot(
            type = "pull",
            color = 0xFFFFA000
        )  // жёлтый
        if (date.dayOfMonth % 5 == 0) dots += Dot(type = "cardio", color = 0xFFE53935) // красный
        return DayMetadata(dots)
    }
}