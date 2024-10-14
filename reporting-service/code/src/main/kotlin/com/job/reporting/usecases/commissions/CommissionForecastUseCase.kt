package com.job.reporting.usecases.commissions

import com.job.reporting.infrastructure.repositories.JobCommissionForecastRepository
import com.job.reporting.infrastructure.repositories.PaginatedResult
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CommissionForecastUseCase(
    private val jobCommissionForecastRepository: JobCommissionForecastRepository,
) {
    suspend fun execute(pageIndex: Int): Mono<PaginatedResult<ForecastCommissionDto>> {
        return jobCommissionForecastRepository.getPaginatedResult(pageIndex)
    }
}
