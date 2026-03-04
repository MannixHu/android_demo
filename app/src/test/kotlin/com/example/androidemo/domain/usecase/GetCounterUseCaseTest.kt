package com.example.androidemo.domain.usecase

import com.example.androidemo.domain.repository.CounterRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetCounterUseCaseTest {
    private val mockRepository: CounterRepository = mockk()
    private lateinit var useCase: GetCounterUseCase

    @Before
    fun setup() {
        useCase = GetCounterUseCase(mockRepository)
    }

    @Test
    fun testInvokeCallsRepositoryGetCounter() = runTest {
        every { mockRepository.getCounter() } returns flowOf(42)
        useCase()
        verify { mockRepository.getCounter() }
    }

    @Test
    fun testInvokeReturnsRepositoryFlow() = runTest {
        val expectedValue = 10
        every { mockRepository.getCounter() } returns flowOf(expectedValue)

        val flow = useCase()
        var collectedValue = 0
        flow.collect { value ->
            collectedValue = value
        }

        assertEquals(expectedValue, collectedValue)
    }
}
