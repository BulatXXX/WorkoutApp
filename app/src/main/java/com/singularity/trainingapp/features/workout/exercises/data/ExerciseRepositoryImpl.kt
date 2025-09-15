package com.singularity.trainingapp.features.workout.exercises.data

import com.singularity.trainingapp.features.workout.data.local.dao.ExerciseDao
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import com.singularity.trainingapp.features.workout.exercises.domain.ExerciseRepository
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val exerciseDao: ExerciseDao) :
    ExerciseRepository {
    override fun observeAll() = exerciseDao.observeAll()

    override fun getById(id: Long) = exerciseDao.getById(id)

    override suspend fun searchByName(query: String) = exerciseDao.searchByName(query)

    override suspend fun upsert(exercise: Exercise) = exerciseDao.upsert(exercise)

    override suspend fun deleteById(id: Long) = exerciseDao.deleteById(id)
}