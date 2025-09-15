package com.singularity.trainingapp.features.workout.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    //GET
    @Query("SELECT * FROM exercises WHERE isDeleted = 0 ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE remoteId = :remoteId LIMIT 1")
    fun getByRemoteId(remoteId: String): Flow<Exercise?>

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    fun getById(id: Long): Flow<Exercise?>

    @Query(
        """
        SELECT * FROM exercises 
        WHERE isDeleted = 0 AND name LIKE '%' || :query || '%'
        ORDER BY name COLLATE NOCASE
    """
    )
    suspend fun searchByName(query: String): List<Exercise>

    //UPSERT
    @Upsert
    suspend fun upsert(exercise: Exercise)

    @Upsert
    suspend fun upsertAll(items: List<Exercise>)

    //DELETE
    @Query("DELETE FROM exercises WHERE id = :id LIMIT 1")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM exercises WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: String)

}