package com.job.reporting.infrastructure.repositories

import com.job.reporting.usecases.commissions.ForecastCommissionDto
import com.job.reporting.utilities.Constant.CURRENCY_SCALE
import com.job.reporting.utilities.Constant.PAGE_SIZE
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class JobCommissionForecastRepository(
    private val databaseClient: DatabaseClient
) {
    fun getPaginatedResult(page: Int, size: Int = PAGE_SIZE): Mono<PaginatedResult<ForecastCommissionDto>> {
        val dataFlux = getPaginatedClientCommission(
            size = size,
            offset = page * size
        ).collectList()
        val totalClients = countAll()

        return totalClients.zipWith(dataFlux) { totalItems, data ->
            val totalPages = (totalItems / size).toInt() + if (totalItems % size > 0) 1 else 0
            PaginatedResult(
                rows = data,
                totalItems = totalItems,
                totalPages = totalPages,
                currentPage = page
            )
        }
    }

    private fun getPaginatedClientCommission(size: Int, offset: Int): Flux<ForecastCommissionDto> {
        val query = """
            SELECT clientReferenceId, clientName, totalCommission * COALESCE(er.exchange_rate_to_usd, 0) AS totalCommission
            FROM (
                     SELECT c.client_reference_id AS clientReferenceId, 
                            c.name AS clientName, 
                            c.exchange_rate_id AS exchangeRateId,
                            COALESCE(SUM(j.forecast_commission), 0) AS totalCommission
                     FROM clients c
                         LEFT JOIN jobs j ON c.id = j.client_id
                     GROUP BY c.id
                     LIMIT :limit OFFSET :offset
                 ) AS c
            LEFT JOIN exchange_rates er ON c.exchangeRateId = er.id
        """.trimIndent()

        return databaseClient
            .sql(query)
            .bind("limit", size)
            .bind("offset", offset)
            .map { row ->
                ForecastCommissionDto(
                    clientReferenceId = row.get("clientReferenceId", String::class.java) ?: "",
                    clientName = row.get("clientName", String::class.java) ?: "",
                    totalCommission = (row.get("totalCommission", BigDecimal::class.java) ?: BigDecimal.ZERO)
                        .setScale(CURRENCY_SCALE, RoundingMode.HALF_EVEN)
                )
            }.all()
    }

    private fun countAll(): Mono<Long> {
        return databaseClient.sql("SELECT COUNT(*) AS count FROM clients")
            .map { row -> row.get("count", String::class.java)?.toLong() ?: 0L }
            .one()
    }
}