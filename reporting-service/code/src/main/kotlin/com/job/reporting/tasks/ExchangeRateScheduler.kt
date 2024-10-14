package com.job.reporting.tasks

import com.job.reporting.usecases.exchangerate.UpdateExchangeRateUseCase
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ExchangeRateScheduler (
    private val updateExchangeRateUseCase: UpdateExchangeRateUseCase
) {
    @Scheduled(cron="0 1 0 * * *")
    fun updateExchangeRate() {
        println("Cron Task - Current Time: ${LocalDateTime.now()}")
        runBlocking {
            updateExchangeRateUseCase.execute()
        }
    }
}
