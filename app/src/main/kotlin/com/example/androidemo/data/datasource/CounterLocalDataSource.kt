package com.example.androidemo.data.datasource

import com.example.androidemo.data.local.CounterDao
import com.example.androidemo.data.local.CounterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CounterLocalDataSource(private val counterDao: CounterDao) {
    fun getCounter(): Flow<CounterEntity?> = counterDao.getCounter()

    suspend fun updateCounter(entity: CounterEntity) {
        counterDao.updateCounter(entity)
    }

    suspend fun insertCounter(entity: CounterEntity) {
        counterDao.insertCounter(entity)
    }

    /**
     * Intelligently updates or inserts the counter.
     * If a record exists, it updates it; otherwise, it inserts a new one.
     * This prevents crashes when trying to update non-existent records.
     */
    suspend fun updateOrInsertCounter(entity: CounterEntity) {
        val existing = counterDao.getCounter().firstOrNull()
        if (existing != null) {
            counterDao.updateCounter(entity)
        } else {
            counterDao.insertCounter(entity)
        }
    }
}
