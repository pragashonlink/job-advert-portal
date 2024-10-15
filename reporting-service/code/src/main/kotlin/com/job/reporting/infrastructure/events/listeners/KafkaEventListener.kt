package com.job.reporting.infrastructure.events.listeners

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.reporting.infrastructure.events.BaseEvent
import com.job.reporting.infrastructure.events.EventUseCase
import kotlinx.coroutines.runBlocking
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class KafkaEventListener(
    private val objectMapper: ObjectMapper,
    private val useCases: Collection<EventUseCase<out BaseEvent>>
) {
    @KafkaListener(
        topics = [
            "\${kafka.topic.application}"
        ],
        groupId = "\${kafka.group-id.reporting}",
    )
    fun handle(
        @Payload message: Map<String, Any>,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
    ) {
        println("Received message: $message")
        val useCase = useCases.first { it.eventType.toString() == message["eventType"] }
        val dto = objectMapper.convertValue(message, useCase.eventClass.java)
        println(
            "Processing message: '${dto}', from topic: '${topic}', by use case: ${useCase::class.java.simpleName}"
        )
        runBlocking {
            val a = useCase as EventUseCase<BaseEvent>
            a.execute(dto)
        }
    }
}
