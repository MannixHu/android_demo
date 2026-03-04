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

class DecrementCounterUseCaseTest {
    private val mockRepository: CounterRepository = mockk()
    private lateinit var useCase: DecrementCounterUseCase

    @Before
    fun setup() {
        coEvery { mockRepository.decrementCounter() } just runs
        useCase = DecrementCounterUseCase(mockRepository)
    }

    @Test
    fun testInvokeCallsRepositoryDecrement() = runTest {
        useCase()
        coVerify { mockRepository.decrementCounter() }
    }
}
