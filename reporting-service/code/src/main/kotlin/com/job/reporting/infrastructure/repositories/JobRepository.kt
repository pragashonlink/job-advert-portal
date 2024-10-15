package com.job.reporting.infrastructure.repositories

import com.job.reporting.infrastructure.entities.JobEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JobRepository : CoroutineCrudRepository<JobEntity, Long> {
    suspend fun findByJobReferenceId(jobReferenceId: String): JobEntity?
}