package com.example.androidemo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidemo.domain.usecase.DecrementCounterUseCase
import com.example.androidemo.domain.usecase.GetCounterUseCase
import com.example.androidemo.domain.usecase.IncrementCounterUseCase
import com.example.androidemo.domain.usecase.ResetCounterUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CounterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val mockGetCounterUseCase: GetCounterUseCase = mockk()
    private val mockIncrementCounterUseCase: IncrementCounterUseCase = mockk()
    private val mockDecrementCounterUseCase: DecrementCounterUseCase = mockk()
    private val mockResetCounterUseCase: ResetCounterUseCase = mockk()

    private lateinit var viewModel: CounterViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Setup mock behavior for use cases
        every { mockGetCounterUseCase() } returns flowOf(0)
        coEvery { mockIncrementCounterUseCase() } just runs
        coEvery { mockDecrementCounterUseCase() } just runs
        coEvery { mockResetCounterUseCase() } just runs

        viewModel = CounterViewModel(
            getCounterUseCase = mockGetCounterUseCase,
            incrementCounterUseCase = mockIncrementCounterUseCase,
            decrementCounterUseCase = mockDecrementCounterUseCase,
            resetCounterUseCase = mockResetCounterUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCounterInitialValue() = runTest {
        val value = viewModel.counterState.value
        assertEquals(0, value)
    }

    @Test
    fun testIncrement() = runTest {
        viewModel.increment()
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockIncrementCounterUseCase() }
    }

    @Test
    fun testDecrement() = runTest {
        viewModel.decrement()
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockDecrementCounterUseCase() }
    }

    @Test
    fun testReset() = runTest {
        viewModel.reset()
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockResetCounterUseCase() }
    }

    @Test
    fun testMultipleOperations() = runTest {
        viewModel.increment()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.increment()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.decrement()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 2) { mockIncrementCounterUseCase() }
        coVerify(exactly = 1) { mockDecrementCounterUseCase() }
    }

    @Test
    fun testGetCounterUseCaseIsCalledOnInit() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { mockGetCounterUseCase() }
    }
}
