package com.example.androidemo.domain.usecase

import com.example.androidemo.domain.repository.CounterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class IncrementCounterUseCaseTest {
    private val mockRepository: CounterRepository = mockk()
    private lateinit var useCase: IncrementCounterUseCase

    @Before
    fun setup() {
        coEvery { mockRepository.incrementCounter() } just runs
        useCase = IncrementCounterUseCase(mockRepository)
    }

    @Test
    fun testInvokeCallsRepositoryIncrement() = runTest {
        useCase()
        coVerify { mockRepository.incrementCounter() }
    }
}
