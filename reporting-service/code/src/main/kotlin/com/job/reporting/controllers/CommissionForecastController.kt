package com.job.reporting.controllers

import com.job.reporting.infrastructure.repositories.PaginatedResult
import com.job.reporting.usecases.commissions.CommissionForecastUseCase
import com.job.reporting.usecases.commissions.ForecastCommissionDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/commission_forecast")
@Validated
class CommissionForecastController(
    private val commissionForecastUseCase: CommissionForecastUseCase
) {
    @Operation(summary = "Return commission forecast data for clients")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK"),
    ])
    @GetMapping()
    suspend fun getCommissionForecastReport(
        @RequestParam(name = "page", defaultValue = "0") page: Int,
    ): Mono<PaginatedResult<ForecastCommissionDto>> {
        return commissionForecastUseCase.execute(page)
    }
}