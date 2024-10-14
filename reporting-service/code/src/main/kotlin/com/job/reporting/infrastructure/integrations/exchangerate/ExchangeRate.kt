package com.job.reporting.infrastructure.integrations.exchangerate

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.job.reporting.utilities.MapUtility.Companion.convertToMap
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ExchangeRate(
    private val exchangeRateWebClient: ExchangeRateWebClient,
    private val objectMapper: ObjectMapper
) {
    suspend fun getRate(baseCurrencyCode: String, targetCurrencyCode: String): BigDecimal {
        val response = exchangeRateWebClient.fetch(baseCurrencyCode.lowercase())
        val exchangeRateResponse: Map<String, Any> = objectMapper.readValue(response, object : TypeReference<Map<String, Any>>() {})
        val rates = convertToMap(exchangeRateResponse[baseCurrencyCode.lowercase()])
        val rate = rates[targetCurrencyCode.lowercase()].toString()

        return rate.toBigDecimal()
    }
}
