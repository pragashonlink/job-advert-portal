package com.job.reporting.usecases.commissions

import java.math.BigDecimal

data class ForecastCommissionDto(
    val clientReferenceId: String,
    val clientName: String,
    val totalCommission: BigDecimal
)