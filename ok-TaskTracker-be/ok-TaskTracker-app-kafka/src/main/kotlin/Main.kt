package ru.otus.otuskotlin.TaskTracker.app.kafka

import ru.otus.otuskotlin.marketplace.app.kafka.ConsumerStrategyV1

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()))
    consumer.start()
}
