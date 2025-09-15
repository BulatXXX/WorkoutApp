package com.singularity.trainingapp.features.workout.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "exercise_sets",
    indices = [
        Index("workoutExerciseId"),
        Index("orderInExercise"),
        Index(value = ["workoutExerciseId", "orderInExercise"], unique = true),
        Index("updatedAt"),
        Index("isDeleted"),
    ],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val workoutExerciseId: Long,
    val orderInExercise: Int = 0,

    val durationSec: Int? = null,
    val repeatsCount: Int? = null,
    val weightKg: Float? = null,
    val distanceInMeters: Float? = null,

    val completed: Boolean = false,
    val note: String? = null,

    val remoteId: String? = null,
    val updatedAt: Instant = Instant.now(),
    val isDirty: Boolean = false,
    val isDeleted: Boolean = false,
)


