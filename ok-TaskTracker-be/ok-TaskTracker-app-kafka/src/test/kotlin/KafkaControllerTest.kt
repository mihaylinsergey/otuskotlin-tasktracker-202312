package ru.otus.otuskotlin.marketplace.app.kafka

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import ru.otus.otuskotlin.TaskTracker.app.kafka.AppKafkaConfig
import ru.otus.otuskotlin.TaskTracker.app.kafka.AppKafkaConsumer
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        TaskCreateRequest(
                            task = TaskCreateObject(
                                title = "task title",
                                executor = "task executor",
                                stageList = listOf(
                                    StageList(
                                        id = "1",
                                        stage = "stage one",
                                        value = "1.1",
                                        comment = "comment one"
                                    ),
                                    StageList(
                                        id = "2",
                                        stage = "stage two",
                                        value = "2.1",
                                        comment = "comment two"
                                    )
                                ),
                                deadline = "01.01.2025",
                                address = "some address",
                                comment = "task comment",
                                photo = "https://someurl",
                                taskType = WorkSide.CUSTOMER,
                                visibility = TaskVisibility.PUBLIC,
                            ),
                            debug = TaskDebug(
                                mode = TaskRequestDebugMode.STUB,
                                stub = TaskRequestDebugStubs.SUCCESS,
                            ),
                    )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TaskCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("task title", result.task?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}


