package com.job.reporting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ReportingApplication

fun main(args: Array<String>) {
	runApplication<ReportingApplication>(*args)
}
