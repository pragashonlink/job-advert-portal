package com.job.reporting.infrastructure.entities

import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.time.Instant

@Table(name = "jobs")
data class JobEntity (
    @Id
    val id: Long? = null,
    val clientId: Long,
    val jobReferenceId: String,
    val numberOfVacancies: Int,
    val forecastCommission: BigDecimal,
    val title: String,
    val createdAt: Instant,
    @LastModifiedDate
    val updatedAt: Instant? = null,
)