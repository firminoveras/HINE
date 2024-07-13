package com.firmino.hinedigital.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.firmino.hinedigital.model.entity.Evaluation
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluationDao {
    @Query("SELECT * FROM evaluation_table")
    fun getAll(): Flow<List<Evaluation>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(evaluation: Evaluation)

    @Query("DELETE FROM evaluation_table WHERE id = :id")
    suspend fun delete(id: Int)

    @Update(onConflict = REPLACE)
    suspend fun update(evaluation: Evaluation)
}