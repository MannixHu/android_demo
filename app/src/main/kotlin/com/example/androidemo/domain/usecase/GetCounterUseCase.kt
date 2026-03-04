package com.example.androidemo.domain.usecase

import com.example.androidemo.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow

class GetCounterUseCase(private val repository: CounterRepository) {
    operator fun invoke(): Flow<Int> = repository.getCounter()
}
