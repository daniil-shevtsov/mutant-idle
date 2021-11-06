package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.main.domain.time.StartTimeUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

class IdleGameViewModel @Inject constructor(
    private val startTime: StartTimeUseCase,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun onStart() {
        scope.launch {
            startTime()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

}