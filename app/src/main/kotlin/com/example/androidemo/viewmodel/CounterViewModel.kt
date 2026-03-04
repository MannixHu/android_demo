package com.example.androidemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidemo.domain.usecase.DecrementCounterUseCase
import com.example.androidemo.domain.usecase.GetCounterUseCase
import com.example.androidemo.domain.usecase.IncrementCounterUseCase
import com.example.androidemo.domain.usecase.ResetCounterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val getCounterUseCase: GetCounterUseCase,
    private val incrementCounterUseCase: IncrementCounterUseCase,
    private val decrementCounterUseCase: DecrementCounterUseCase,
    private val resetCounterUseCase: ResetCounterUseCase
) : ViewModel() {
    private val _counterState = MutableStateFlow(0)
    val counterState: StateFlow<Int> = _counterState.asStateFlow()

    init {
        viewModelScope.launch {
            getCounterUseCase().collect { value ->
                _counterState.value = value
            }
        }
    }

    fun increment() {
        viewModelScope.launch {
            incrementCounterUseCase()
        }
    }

    fun decrement() {
        viewModelScope.launch {
            decrementCounterUseCase()
        }
    }

    fun reset() {
        viewModelScope.launch {
            resetCounterUseCase()
        }
    }
}
