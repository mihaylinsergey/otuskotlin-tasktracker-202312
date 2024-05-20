package ru.otus.otuskotlin.marketplace.app.kafka

import TrackerContext
import apiV1RequestDeserialize
import apiV1ResponseSerialize
import fromTransport
import ru.otus.otuskotlin.TaskTracker.api.v1.models.IRequest
import ru.otus.otuskotlin.TaskTracker.api.v1.models.IResponse
import ru.otus.otuskotlin.TaskTracker.app.kafka.AppKafkaConfig
import ru.otus.otuskotlin.TaskTracker.app.kafka.ConsumerStrategy
import ru.otus.otuskotlin.TaskTracker.app.kafka.InputOutputTopics
import toTransportTask

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: TrackerContext): String {
        val response: IResponse = source.toTransportTask()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: TrackerContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
