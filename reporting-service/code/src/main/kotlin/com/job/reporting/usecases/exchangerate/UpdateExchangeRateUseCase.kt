package com.job.reporting.usecases.exchangerate

import com.job.reporting.infrastructure.entities.ExchangeRateEntity
import com.job.reporting.infrastructure.integrations.exchangerate.ExchangeRate
import com.job.reporting.infrastructure.repositories.ExchangeRateRepository
import com.job.reporting.utilities.Constant.SYSTEM_CURRENCY
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class UpdateExchangeRateUseCase(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val exchangeRate: ExchangeRate,
) {
    suspend fun execute() {
        val exchangeRateEntities = mutableListOf<ExchangeRateEntity>()
        exchangeRateRepository.findAll().toList().forEach {
            val exchangeRate = exchangeRate.getRate(
                baseCurrencyCode = it.currencyCode,
                targetCurrencyCode = SYSTEM_CURRENCY
            )

            exchangeRateEntities.add(it.copy(exchangeRateToUsd = exchangeRate))
        }
        exchangeRateRepository.saveAll(exchangeRateEntities)
    }
}