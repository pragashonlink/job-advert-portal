package com.job.reporting.usecases.applications.submit

import com.job.reporting.infrastructure.entities.ApplicationEntity
import com.job.reporting.infrastructure.repositories.ApplicationRepository
import com.job.reporting.infrastructure.repositories.JobRepository
import com.job.reporting.domain.services.ForecastCommissionCalculationService
import com.job.reporting.infrastructure.events.EventType
import com.job.reporting.infrastructure.events.EventUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ApplicationSubmittedUseCase(
    private val applicationRepository: ApplicationRepository,
    private val jobRepository: JobRepository,
    private val forecastCommissionCalculationService: ForecastCommissionCalculationService,
): EventUseCase<ApplicationSubmittedEvent> {
    override val eventType = EventType.APPLICATION_SUBMITTED
    override val eventClass = ApplicationSubmittedEvent::class

    @Transactional
    override suspend fun execute(message: ApplicationSubmittedEvent) {
        val job = jobRepository.findByJobReferenceId(message.jobReferenceId)
            ?: throw Exception("Job reference id is not valid ${message.jobReferenceId}")
        requireNotNull(job.id) { "Job ID is invalid." }

        applicationRepository.save(
            ApplicationEntity(
                jobId = job.id,
                applicationReferenceId = message.applicationReferenceId,
                expectedSalary = message.expectedSalary,
                createdAt = Instant.now()
            )
        )
        jobRepository.save(
            job.copy(
                forecastCommission = forecastCommissionCalculationService.calculate(job.id, job.numberOfVacancies)
            )
        )

        println("application saved and commission updated successfully")
    }

}