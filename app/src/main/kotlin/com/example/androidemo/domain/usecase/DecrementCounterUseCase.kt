package com.example.androidemo.domain.usecase

import com.example.androidemo.domain.repository.CounterRepository

class DecrementCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke() {
        repository.decrementCounter()
    }
}
