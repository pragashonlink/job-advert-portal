package com.job.reporting.infrastructure.repositories

import com.job.reporting.infrastructure.entities.ClientEntity
import com.job.reporting.infrastructure.entities.ExchangeRateEntity
import com.job.reporting.infrastructure.entities.JobEntity
import com.job.reporting.usecases.applications.submit.ApplicationSubmittedEvent
import com.job.reporting.usecases.applications.submit.ApplicationSubmittedUseCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@SpringBootTest
@DirtiesContext
@Tag("integrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = ["test"])
class JobCommissionForecastRepositoryIntegrationTest {
    @Autowired private lateinit var applicationRepository: ApplicationRepository
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var clientRepository: ClientRepository
    @Autowired private lateinit var exchangeRateRepository: ExchangeRateRepository
    @Autowired private lateinit var jobCommissionForecastRepository: JobCommissionForecastRepository
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
    fun `should return paginated result with rows`() {
        seedApplications()

        val paginatedResult = jobCommissionForecastRepository.getPaginatedResult(page = 0)

        paginatedResult.block()?.totalItems shouldBe 2
        paginatedResult.block()?.totalPages shouldBe 1
        paginatedResult.block()?.currentPage shouldBe 0
        paginatedResult.block()?.rows?.count() shouldBe 2
        println("paginated result: $paginatedResult")
    }

    @Test
    fun `should return paginated result with empty rows when page is greater`() {
        seedApplications()

        val paginatedResult = jobCommissionForecastRepository.getPaginatedResult(page = 2)

        paginatedResult.block() shouldNotBe null
        paginatedResult.block()?.totalItems shouldBe 2
        paginatedResult.block()?.totalPages shouldBe 1
        paginatedResult.block()?.currentPage shouldBe 2
        paginatedResult.block()?.rows?.count() shouldBe 0
        println("paginated result: $paginatedResult")
    }

    private fun seedApplications() = runBlocking {
        val jobSe = seedJob(
            name = "ABC Company",
            title = "Software engineer",
            numberOfVacancies = 2
        )
        val applicationJobSeDto = ApplicationSubmittedEvent(
            expectedSalary = BigDecimal("10000"),
            jobReferenceId = jobSe.jobReferenceId,
            applicationReferenceId = UUID.randomUUID().toString()
        )
        applicationSubmittedUseCase.execute(applicationJobSeDto)

        val jobSSe = seedJob(
            name = "A2B Company",
            title = "Senior Software engineer",
            numberOfVacancies = 3
        )
        val applicationJobSSeDto = ApplicationSubmittedEvent(
            expectedSalary = BigDecimal("20000"),
            jobReferenceId = jobSSe.jobReferenceId,
            applicationReferenceId = UUID.randomUUID().toString()
        )
        applicationSubmittedUseCase.execute(applicationJobSSeDto)
    }

    private fun seedJob (name: String, title: String, numberOfVacancies: Int ): JobEntity = runBlocking {
        val client = seedClient(name)
        jobRepository.save(
            JobEntity(
                jobReferenceId = UUID.randomUUID().toString(),
                title = title,
                numberOfVacancies = numberOfVacancies,
                clientId = client.id!!,
                forecastCommission = BigDecimal.ZERO,
                createdAt = Instant.now()
            )
        )
    }

    private fun seedClient (name: String): ClientEntity = runBlocking {
        val clientReferenceId = UUID.randomUUID().toString()
        val exchangeRateEntity = seedExchangeRate()
        clientRepository.save(
            ClientEntity(
                clientReferenceId = clientReferenceId,
                countryCode = "JPY",
                name = name,
                exchangeRateId = exchangeRateEntity.id!!,
                createdAt = Instant.now()
            )
        )
    }

    private fun seedExchangeRate(): ExchangeRateEntity = runBlocking {
        exchangeRateRepository.save(
            ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("0.0033432234"),
                currencyCode = "JPY"
            )
        )
    }

}