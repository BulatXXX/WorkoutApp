package com.singularity.trainingapp.features.workout.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime


@Entity(
    tableName = "workouts",
    indices = [
        Index("date"),
        Index("remoteId"),
        Index("updatedAt"),
        Index("isDeleted"),
        Index("isDirty"),
    ],
)
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val date: LocalDate,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,

    val title: String? = null,
    val note: String? = null,

    val remoteId: String? = null,
    val updatedAt: Instant = Instant.now(),
    val isDirty: Boolean = false,
    val isDeleted: Boolean = false,
)
