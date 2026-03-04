package com.example.androidemo.data.repository

import com.example.androidemo.data.datasource.CounterLocalDataSource
import com.example.androidemo.data.local.CounterEntity
import com.example.androidemo.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class CounterRepositoryImpl(
    private val counterLocalDataSource: CounterLocalDataSource
) : CounterRepository {
    override fun getCounter(): Flow<Int> {
        return counterLocalDataSource.getCounter()
            .map { entity -> entity?.value ?: 0 }
    }

    override suspend fun incrementCounter() {
        val current = counterLocalDataSource.getCounter()
            .firstOrNull()?.value ?: 0
        counterLocalDataSource.updateCounter(
            CounterEntity(value = current + 1)
        )
    }

    override suspend fun decrementCounter() {
        val current = counterLocalDataSource.getCounter()
            .firstOrNull()?.value ?: 0
        counterLocalDataSource.updateCounter(
            CounterEntity(value = current - 1)
        )
    }

    override suspend fun resetCounter() {
        counterLocalDataSource.updateCounter(CounterEntity(value = 0))
    }
}
