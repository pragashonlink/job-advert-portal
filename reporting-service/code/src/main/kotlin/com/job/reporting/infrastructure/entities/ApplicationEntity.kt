package com.job.reporting.infrastructure.entities

import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.time.Instant

@Table(name = "applications")
data class ApplicationEntity(
    @Id
    val id: Long? = null,
    val jobId: Long,
    val applicationReferenceId: String,
    val expectedSalary: BigDecimal,
    val createdAt: Instant,
    @LastModifiedDate
    val updatedAt: Instant? = null,
)
