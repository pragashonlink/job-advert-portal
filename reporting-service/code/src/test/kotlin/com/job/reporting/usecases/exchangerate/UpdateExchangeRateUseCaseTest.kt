package com.job.reporting.usecases.exchangerate

import com.job.reporting.infrastructure.entities.ExchangeRateEntity
import com.job.reporting.infrastructure.integrations.exchangerate.ExchangeRate
import com.job.reporting.infrastructure.repositories.ExchangeRateRepository
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class UpdateExchangeRateUseCaseTest {
    @MockK
    private lateinit var exchangeRateRepository: ExchangeRateRepository
    @MockK
    private lateinit var exchangeRate: ExchangeRate
    private lateinit var updateExchangeRateUseCase: UpdateExchangeRateUseCase

    @BeforeEach
    fun setUp() {
        updateExchangeRateUseCase = UpdateExchangeRateUseCase(
            exchangeRateRepository =  exchangeRateRepository,
            exchangeRate = exchangeRate
        )
    }

    @AfterEach
    fun resetAndVerify() {
        clearAllMocks()
    }

    @Test
    fun `should update exchange rate successfully`() {
        runTest {
            val exchangeRates = flowOf(
                ExchangeRateEntity(
                    currencyCode = "JPY",
                    exchangeRateToUsd = BigDecimal.ZERO,
                ),
                ExchangeRateEntity(
                    currencyCode = "LKR",
                    exchangeRateToUsd = BigDecimal.ZERO,
                )
            )
            coEvery { exchangeRateRepository.findAll() } coAnswers { exchangeRates }
            coEvery { exchangeRate.getRate("JPY", any<String>()) } coAnswers { BigDecimal("0.003432234") }
            coEvery { exchangeRate.getRate("LKR", any<String>()) } coAnswers { BigDecimal("0.004545657") }
            val slot = slot<MutableList<ExchangeRateEntity>>()
            coEvery { exchangeRateRepository.saveAll(capture(slot)) } coAnswers { emptyFlow() }

            updateExchangeRateUseCase.execute()

            val expectedList = mutableListOf<ExchangeRateEntity>(
                ExchangeRateEntity(
                    currencyCode = "JPY",
                    exchangeRateToUsd = BigDecimal("0.003432234"),
                ),
                ExchangeRateEntity(
                    currencyCode = "LKR",
                    exchangeRateToUsd = BigDecimal("0.004545657"),
                )
            )
            slot.captured shouldBe expectedList
        }
    }

}