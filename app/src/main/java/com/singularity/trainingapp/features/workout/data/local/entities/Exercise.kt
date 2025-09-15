package com.singularity.trainingapp.features.workout.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "exercises",
    indices = [
        Index(value = ["remoteId"], unique = true),
        Index("name"),
        Index("updatedAt"),
        Index("isDeleted"),
        Index("isDirty"),
    ],
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val remoteId: String? = null,
    val name: String,
    val description: String? = null,

    val updatedAt: Instant = Instant.now(),
    val isDirty: Boolean = false,
    val isDeleted: Boolean = false,

    val hasDuration: Boolean = true,

    val hasRepeats: Boolean = true,
    val hasDistance: Boolean = false,
    val hasWeight: Boolean = false,
)



