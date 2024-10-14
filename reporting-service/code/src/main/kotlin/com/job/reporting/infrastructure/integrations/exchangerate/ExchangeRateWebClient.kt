package com.job.reporting.infrastructure.integrations.exchangerate

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ExchangeRateWebClient(
    private val exchangeRateConfig: ExchangeRateConfig,
) {
    suspend fun fetch(currencyCode: String): String {
        return WebClient.builder()
            .baseUrl(exchangeRateConfig.baseUrl)
            .build()
            .get()
            .uri("/${currencyCode}.json")
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()
    }
}