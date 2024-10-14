package com.job.reporting.infrastructure.integrations.exchangerate

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class ExchangeRateTest {
    @MockK
    private lateinit var exchangeRateWebClient: ExchangeRateWebClient
    private lateinit var exchangeRate: ExchangeRate

    @BeforeEach
    fun setUp() {
        exchangeRate = ExchangeRate(
            exchangeRateWebClient = exchangeRateWebClient,
            objectMapper = jacksonObjectMapper()
        )
    }

    @AfterEach
    fun resetAndVerify() {
        clearAllMocks()
    }

    @Test
    fun `should return zero commission when job applications are empty`() {
        runTest {
            coEvery { exchangeRateWebClient.fetch(any()) } coAnswers { exchangeRateResponse }
            val rate = exchangeRate.getRate(
                baseCurrencyCode = "JPY",
                targetCurrencyCode = "USD"
            )

            rate shouldBe BigDecimal("1.08498675")
        }
    }

    private companion object {
        val exchangeRateResponse = """
            {
            	"date": "2024-03-06",
            	"jpy": {
            		"uni": 0.083041117,
            		"uos": 4.56442563,
            		"upi": 1996.36570674,
            		"uqc": 0.11660279,
            		"usd": 1.08498675,
            		"usdc": 1.08498439,
            		"usdd": 1.08589846
            	}
            }
        """.trimIndent()
    }

}