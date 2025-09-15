package com.singularity.trainingapp.features.workout.di

import android.content.Context
import androidx.room.Room
import com.singularity.trainingapp.features.workout.data.WorkoutDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutDbModule {

    @Provides
    @Singleton
    fun provideWorkoutDatabase(
        @ApplicationContext context: Context
    ): WorkoutDatabase =
        Room.databaseBuilder(
            context,
            WorkoutDatabase::class.java,
            "workout.db"
        ).build()
}