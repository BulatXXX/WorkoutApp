package com.singularity.trainingapp.features.workout.exercises.di

import com.singularity.trainingapp.features.workout.exercises.data.ExerciseRepositoryImpl
import com.singularity.trainingapp.features.workout.exercises.domain.ExerciseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExerciseModule {
    @Binds
    @Singleton
    abstract fun bindExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository
}
