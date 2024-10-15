package com.job.reporting.infrastructure.repositories

import com.job.reporting.infrastructure.entities.ClientEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ClientRepository : CoroutineCrudRepository<ClientEntity, Long>