package com.job.reporting.config

import io.mockk.mockk
import org.slf4j.Logger
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class IntegrationTestConfig {
    @Primary
    @Bean
    fun mockLogger(): Logger {
        return mockk<Logger>(relaxed = true)
    }
}
