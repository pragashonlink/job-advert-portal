package com.job.reporting.infrastructure.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table(name = "exchange_rates")
data class ExchangeRateEntity(
    @Id
    val id: Long? = null,
    val currencyCode: String,
    val exchangeRateToUsd: BigDecimal,
)
