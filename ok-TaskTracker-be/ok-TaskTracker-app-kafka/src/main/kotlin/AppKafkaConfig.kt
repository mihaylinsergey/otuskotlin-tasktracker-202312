package ru.otus.otuskotlin.TaskTracker.app.kafka

import TrackerCorSettings
import ru.otus.otuskotlin.TaskTracker.logging.common.TrackerLoggerProvider
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import ru.otus.otuskotlin.marketplace.app.common.ITrackerAppSettings
import ru.otus.otuskotlin.tracker.logging.jvm.trackerLoggerLogback


class AppKafkaConfig(
    val kafkaHosts: String = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    override val corSettings: TrackerCorSettings = TrackerCorSettings(
        loggerProvider = TrackerLoggerProvider { trackerLoggerLogback(it) }
    ),
    override val processor: TrackerTaskProcessor = TrackerTaskProcessor(corSettings),
): ITrackerAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { System.getenv(KAFKA_HOST_VAR) ?: "localhost:9092" }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "tracker" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "tracker-task-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "tracker-task-v1-out" }
    }
}
