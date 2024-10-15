package com.job.reporting.usecases.applications.submit

import com.job.reporting.infrastructure.events.BaseEvent
import com.job.reporting.infrastructure.events.EventType
import java.math.BigDecimal

data class ApplicationSubmittedEvent(
    val jobReferenceId: String,
    val applicationReferenceId: String,
    val expectedSalary: BigDecimal
) : BaseEvent {
    override val eventType = EventType.APPLICATION_SUBMITTED
}