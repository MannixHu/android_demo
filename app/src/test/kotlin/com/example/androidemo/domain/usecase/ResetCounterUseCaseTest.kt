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

class ResetCounterUseCaseTest {
    private val mockRepository: CounterRepository = mockk()
    private lateinit var useCase: ResetCounterUseCase

    @Before
    fun setup() {
        coEvery { mockRepository.resetCounter() } just runs
        useCase = ResetCounterUseCase(mockRepository)
    }

    @Test
    fun testInvokeCallsRepositoryReset() = runTest {
        useCase()
        coVerify { mockRepository.resetCounter() }
    }
}
