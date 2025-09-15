package com.singularity.trainingapp.features.workout.exercises.domain

import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun observeAll(): Flow<List<Exercise>>

    fun getById(id: Long): Flow<Exercise?>

    suspend fun searchByName(query: String): List<Exercise>

    suspend fun upsert(exercise: Exercise)

    suspend fun deleteById(id: Long)
}