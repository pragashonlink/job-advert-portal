package com.job.reporting.config

import com.job.reporting.infrastructure.integrations.exchangerate.ExchangeRateConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ExchangeRateConfig::class)
class AppConfig