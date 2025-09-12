package com.singularity.trainingapp.features.workout.schedule.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "workout_exercises",
    indices = [
        Index("workoutId"),
        Index("exerciseId"),
        Index("orderInWorkout"),
        Index(
            value = ["workoutId", "orderInWorkout"],
            unique = true
        ),
        Index("remoteId"),
        Index("updatedAt")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val workoutId: Long,
    val exerciseId: Long? = null,

    val orderInWorkout: Int = 0,

    val remoteId: String? = null,
    val updatedAt: Instant = Instant.now(),
    val isDirty: Boolean = false,
    val isDeleted: Boolean = false,
)
