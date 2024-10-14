package com.job.reporting.infrastructure.integrations.exchangerate

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "integrations.exchange-rate")
data class ExchangeRateConfig(
    val baseUrl: String
)