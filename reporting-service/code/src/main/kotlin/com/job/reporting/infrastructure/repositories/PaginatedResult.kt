package com.job.reporting.infrastructure.repositories

data class PaginatedResult<T>(
    val rows: List<T>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int
)