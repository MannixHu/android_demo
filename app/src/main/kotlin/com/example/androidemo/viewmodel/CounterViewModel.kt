package com.example.androidemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidemo.domain.usecase.DecrementCounterUseCase
import com.example.androidemo.domain.usecase.GetCounterUseCase
import com.example.androidemo.domain.usecase.IncrementCounterUseCase
import com.example.androidemo.domain.usecase.ResetCounterUseCase
import com.example.androidemo.util.UpdateManager
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
    private val resetCounterUseCase: ResetCounterUseCase,
    private val updateManager: UpdateManager
) : ViewModel() {
    private val _counterState = MutableStateFlow(0)
    val counterState: StateFlow<Int> = _counterState.asStateFlow()

    // 用于触发更新检查的事件
    private val _triggerUpdateCheck = MutableStateFlow(false)
    val triggerUpdateCheck: StateFlow<Boolean> = _triggerUpdateCheck.asStateFlow()

    init {
        viewModelScope.launch {
            getCounterUseCase().collect { value ->
                _counterState.value = value
            }
        }

        // 应用启动时检查更新（如果需要）
        checkUpdateOnAppStart()
    }

    private fun checkUpdateOnAppStart() {
        if (updateManager.shouldCheckForUpdates()) {
            _triggerUpdateCheck.value = true
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

    fun resetUpdateCheckTrigger() {
        _triggerUpdateCheck.value = false
    }
}
