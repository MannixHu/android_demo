package com.example.androidemo.data.datasource

import com.example.androidemo.data.local.CounterDao
import com.example.androidemo.data.local.CounterEntity
import kotlinx.coroutines.flow.Flow

class CounterLocalDataSource(private val counterDao: CounterDao) {
    fun getCounter(): Flow<CounterEntity?> = counterDao.getCounter()

    suspend fun updateCounter(entity: CounterEntity) {
        counterDao.updateCounter(entity)
    }

    suspend fun insertCounter(entity: CounterEntity) {
        counterDao.insertCounter(entity)
    }
}
