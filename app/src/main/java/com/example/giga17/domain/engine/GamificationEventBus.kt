package com.example.giga17.domain.engine

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GamificationEventBus {
    private val _events = MutableSharedFlow<GamificationEvent>(extraBufferCapacity = 10)
    val events = _events.asSharedFlow()

    suspend fun sendEvent(event: GamificationEvent) {
        _events.emit(event)
    }
}
