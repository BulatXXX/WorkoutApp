package com.singularity.trainingapp.features.workout.schedule.di

import com.singularity.trainingapp.features.workout.schedule.data.ScheduleRepositoryImpl
import com.singularity.trainingapp.features.workout.schedule.domain.BuildCalendarWindowUseCase
import com.singularity.trainingapp.features.workout.schedule.domain.CalendarPagingPolicy
import com.singularity.trainingapp.features.workout.schedule.domain.DefaultCalendarPagingPolicy
import com.singularity.trainingapp.features.workout.schedule.domain.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleModule {
    @Provides
    fun providePagingPolicy(): CalendarPagingPolicy =
        DefaultCalendarPagingPolicy()

    @Provides
    fun provideBuildCalendarWindowUseCase(
        policy: CalendarPagingPolicy,
        repo: ScheduleRepository
    ) = BuildCalendarWindowUseCase(policy, repo)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(impl: ScheduleRepositoryImpl): ScheduleRepository
}