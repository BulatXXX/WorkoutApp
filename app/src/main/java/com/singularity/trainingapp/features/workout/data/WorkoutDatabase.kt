package com.singularity.trainingapp.features.workout.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.singularity.trainingapp.features.workout.data.local.TimeConverters
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import com.singularity.trainingapp.features.workout.data.local.entities.ExerciseSet
import com.singularity.trainingapp.features.workout.data.local.entities.Workout
import com.singularity.trainingapp.features.workout.data.local.entities.WorkoutExercise

@Database(
    entities = [
        Exercise::class,
        ExerciseSet::class,
        Workout::class,
        WorkoutExercise::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(TimeConverters::class)
abstract class WorkoutDatabase : RoomDatabase() {
}