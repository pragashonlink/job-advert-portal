package com.job.reporting.infrastructure.events

interface BaseEvent {
    val eventType: EventType
}