package com.example.androidemo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter_table")
data class CounterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val value: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
