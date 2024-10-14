package com.job.reporting.usecases.applications.submit

import com.job.reporting.infrastructure.entities.ApplicationEntity
import io.kotest.matchers.nulls.shouldNotBeNull
import com.job.reporting.infrastructure.entities.ClientEntity
import com.job.reporting.infrastructure.entities.ExchangeRateEntity
import com.job.reporting.infrastructure.entities.JobEntity
import com.job.reporting.infrastructure.repositories.ApplicationRepository
import com.job.reporting.infrastructure.repositories.ClientRepository
import com.job.reporting.infrastructure.repositories.ExchangeRateRepository
import com.job.reporting.infrastructure.repositories.JobRepository
import com.job.reporting.utilities.Constant.CURRENCY_SCALE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import kotlinx.coroutines.test.runTest
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@SpringBootTest
@DirtiesContext
@Tag("integrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = ["test"])
class ApplicationSubmittedUseCaseIntegrationTest() {
    @Autowired private lateinit var applicationRepository: ApplicationRepository
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var clientRepository: ClientRepository
    @Autowired private lateinit var exchangeRateRepository: ExchangeRateRepository
    @Autowired private lateinit var applicationSubmittedUseCase: ApplicationSubmittedUseCase

    @BeforeEach
    fun setUp() {
        runBlocking {
            applicationRepository.deleteAll()
            jobRepository.deleteAll()
            clientRepository.deleteAll()
            exchangeRateRepository.deleteAll()
        }
    }

    @Test
    fun `should successfully save application`() {
        runTest {
            val job = seedJob()
            val applicationReferenceId = UUID.randomUUID().toString()
            val applicationSaveEventDto = ApplicationSubmittedEvent(
                expectedSalary = BigDecimal("10000"),
                jobReferenceId = job.jobReferenceId,
                applicationReferenceId = applicationReferenceId
            )

            applicationSubmittedUseCase.execute(applicationSaveEventDto)

            val timeInstant = Instant.now()
            val expectedApplication = ApplicationEntity(
                applicationReferenceId = applicationReferenceId,
                expectedSalary = BigDecimal("10000").setScale(CURRENCY_SCALE),
                jobId = job.id!!,
                createdAt = timeInstant
            )
            val application = applicationRepository.findAllByApplicationReferenceId(applicationReferenceId)!!
            val jobEntity = jobRepository.findByJobReferenceId(jobReferenceId = job.jobReferenceId)!!
            application.copy(createdAt = timeInstant, id = null, updatedAt = null) shouldBe expectedApplication
            jobEntity.forecastCommission shouldBe BigDecimal("2000").setScale(CURRENCY_SCALE)
        }
    }

    @Test
    fun `should throw exception if job reference id is not found`(){
        runTest {
            val applicationSaveEventDto = ApplicationSubmittedEvent(
                expectedSalary = BigDecimal("10000"),
                jobReferenceId = "invalid-job-reference-id",
                applicationReferenceId = UUID.randomUUID().toString()
            )

            shouldThrow<Exception> {
                applicationSubmittedUseCase.execute(applicationSaveEventDto)
            }
        }
    }

    private fun seedJob (): JobEntity = runBlocking {
        val client = seedClient()
        jobRepository.save(JobEntity(
                jobReferenceId = UUID.randomUUID().toString(),
                title = "Software Engineer",
                numberOfVacancies = 2,
                clientId = client.id!!,
                forecastCommission = BigDecimal.ZERO,
                createdAt = Instant.now()
            )
        )
    }

    private fun seedClient (): ClientEntity = runBlocking {
        val clientReferenceId = UUID.randomUUID().toString()
        val exchangeRateEntity = seedExchangeRate()
        clientRepository.save(ClientEntity(
                clientReferenceId = clientReferenceId,
                countryCode = "JPN",
                name = "ABC Company",
                exchangeRateId = exchangeRateEntity.id!!,
                createdAt = Instant.now()
            )
        )
    }

    private fun seedExchangeRate(): ExchangeRateEntity = runBlocking {
        exchangeRateRepository.save(ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("0.0033432234"),
                currencyCode = "JPY"
            )
        )
    }

}