package com.job.reporting.utilities

import com.job.reporting.infrastructure.entities.ClientEntity
import com.job.reporting.infrastructure.entities.ExchangeRateEntity
import com.job.reporting.infrastructure.entities.JobEntity
import com.job.reporting.infrastructure.repositories.ApplicationRepository
import com.job.reporting.infrastructure.repositories.ClientRepository
import com.job.reporting.infrastructure.repositories.ExchangeRateRepository
import com.job.reporting.infrastructure.repositories.JobRepository
import com.job.reporting.usecases.applications.submit.ApplicationSubmittedEvent
import com.job.reporting.usecases.applications.submit.ApplicationSubmittedUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import kotlin.random.Random

@Profile("local")
@Component
class DataSeeder(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val clientRepository: ClientRepository,
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository,
    private val applicationSubmittedUseCase: ApplicationSubmittedUseCase,
//    private val logger: Logger
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        clearDb()

        val exchangeRates = seedExchangeRates()
        val clients = seedClients(exchangeRates)
        val jobs = seedJobs(clients)
        seedApplications(jobs)
    }

    private fun clearDb() = runBlocking {
        applicationRepository.deleteAll()
        jobRepository.deleteAll()
        clientRepository.deleteAll()
        exchangeRateRepository.deleteAll()
    }

    private fun seedExchangeRates(): Collection<ExchangeRateEntity> = runBlocking {
        val data = listOf(
            ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("0.0034041893"),
                currencyCode = "LKR",
            ),
            ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("1.09695915"),
                currencyCode = "EUR",
            ),
            ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("0.0067034598"),
                currencyCode = "JPY",
            ),
            ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("0.76619954"),
                currencyCode = "SGD",
            ),
            ExchangeRateEntity(
                exchangeRateToUsd = BigDecimal("0.011888923"),
                currencyCode = "INR",
            ),
        )

        val savedRates = exchangeRateRepository.saveAll(data)
        println("Data seeding completed for exchange rates: ${savedRates}", )

        savedRates.toList()
    }

    private fun seedClients(exchangeRates: Collection<ExchangeRateEntity>): Collection<ClientEntity> = runBlocking {
        var data = mutableListOf<ClientEntity>()
        for (i in 1..50) {
            val currencyCode = arrayOf("LKR", "EUR", "JPY", "SGD", "INR").random()
            val countryCodeCurrencyMapping = mapOf(
                Pair("LKR", "LK"),
                Pair("EUR", "EU"),
                Pair("JPY", "JPN"),
                Pair("SGD", "SG"),
                Pair("INR", "IND"),
            )
            data.add(
                ClientEntity(
                    name = randomString(15),
                    clientReferenceId = randomString(10),
                    countryCode = countryCodeCurrencyMapping[currencyCode]!!,
                    exchangeRateId = exchangeRates.first { it.currencyCode.equals(currencyCode)}.id!!,
                    createdAt = Instant.now()
                )
            )
        }

        val clients = clientRepository.saveAll(data)
        println("Data seeding completed for clients: ${clients}")

        clients.toList()
    }

    private fun seedJobs(clients: Collection<ClientEntity>): Collection<JobEntity> = runBlocking {
        var data = mutableListOf<JobEntity>()
        for (i in 1..50) {
            data.add(
                JobEntity(
                    jobReferenceId = randomString(10),
                    title = randomString(15),
                    clientId = clients.random().id!!,
                    numberOfVacancies = Random.nextInt(1,5),
                    forecastCommission = BigDecimal("0"),
                    createdAt = Instant.now()
                )
            )
        }

        val jobs = jobRepository.saveAll(data)
        println("Data seeding completed for jobs: ${jobs}")

        jobs.toList()
    }

    private fun seedApplications(jobs: Collection<JobEntity>) = runBlocking {
        for (i in 1..50) {
            applicationSubmittedUseCase.execute(
                ApplicationSubmittedEvent(
                    applicationReferenceId = randomString(10),
                    jobReferenceId = jobs.random().jobReferenceId,
                    expectedSalary = Random.nextDouble(10000.0, 1000000.0).toBigDecimal()
                )
            )
        }
    }

    private companion object {
        fun randomString(length: Int): String {
            val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { chars.random() }
                .joinToString("")
        }
    }

}