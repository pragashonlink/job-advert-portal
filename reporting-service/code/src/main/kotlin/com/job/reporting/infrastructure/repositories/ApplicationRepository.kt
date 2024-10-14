package com.job.reporting.infrastructure.repositories

import com.job.reporting.infrastructure.entities.ApplicationEntity
import org.apache.kafka.common.protocol.types.Field.Str
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : CoroutineCrudRepository<ApplicationEntity, Long> {
    suspend fun findAllByJobId(jobId: Long): Collection<ApplicationEntity>
    suspend fun findAllByApplicationReferenceId(applicationReferenceId: String): ApplicationEntity?
}