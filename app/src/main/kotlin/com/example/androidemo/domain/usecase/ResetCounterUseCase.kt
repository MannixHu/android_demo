package com.example.androidemo.domain.usecase

import com.example.androidemo.domain.repository.CounterRepository

class ResetCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke() {
        repository.resetCounter()
    }
}
