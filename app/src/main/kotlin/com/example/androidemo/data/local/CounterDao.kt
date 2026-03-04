package com.example.androidemo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {
    @Query("SELECT * FROM counter_table WHERE id = 1")
    fun getCounter(): Flow<CounterEntity?>

    @Update
    suspend fun updateCounter(entity: CounterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(entity: CounterEntity)
}
