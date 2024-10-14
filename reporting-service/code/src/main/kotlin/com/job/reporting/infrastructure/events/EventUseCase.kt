package com.job.reporting.infrastructure.events

import kotlin.reflect.KClass

interface EventUseCase<T: BaseEvent> {
    val eventType: EventType
    val eventClass: KClass<T>

    suspend fun execute(message: T)
}
