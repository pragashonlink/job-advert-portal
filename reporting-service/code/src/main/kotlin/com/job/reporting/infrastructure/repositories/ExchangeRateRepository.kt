package com.job.reporting.infrastructure.repositories

import com.job.reporting.infrastructure.entities.ExchangeRateEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ExchangeRateRepository: CoroutineCrudRepository<ExchangeRateEntity, Long> {
    suspend fun findByCurrencyCode(currencyCode: String): ExchangeRateEntity?
}