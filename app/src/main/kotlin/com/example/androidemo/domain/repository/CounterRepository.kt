package com.example.androidemo.domain.repository

import kotlinx.coroutines.flow.Flow

interface CounterRepository {
    fun getCounter(): Flow<Int>
    suspend fun incrementCounter()
    suspend fun decrementCounter()
    suspend fun resetCounter()
}
