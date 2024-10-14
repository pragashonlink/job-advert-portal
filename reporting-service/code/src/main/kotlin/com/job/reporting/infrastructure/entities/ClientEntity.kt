package com.job.reporting.infrastructure.entities

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "clients")
data class ClientEntity(
    @Id
    val id: Long? = null,
    val exchangeRateId: Long,
    val clientReferenceId: String,
    val name: String,
    val countryCode: String,
    val createdAt: Instant,
    @LastModifiedDate
    val updatedAt: Instant? = null,
)