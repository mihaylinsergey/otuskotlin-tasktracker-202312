package stubs

import TrackerContext
import TrackerCorSettings
import models.TrackerState
import models.TrackerVisibility
import models.TrackerWorkSide
import ru.otus.otuskotlin.TaskTracker.logging.common.LogLevel
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.stubCreateSuccess(title: String, corSettings: TrackerCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания задачи
    """.trimIndent()
    on { stubCase == TrackerStubs.SUCCESS && state == TrackerState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = TrackerState.FINISHING
            val stub = TrackerTaskStub.prepareResult {
                taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                taskRequest.executor.takeIf { it.isNotBlank() }?.also { this.executor = it }
                taskRequest.stageList.takeIf { it.isNotEmpty() }?.also { this.stageList = it }
                taskRequest.deadline.takeIf { it.isNotBlank() }?.also { this.deadline = it }
                taskRequest.address.takeIf { it.isNotBlank() }?.also { this.address = it }
                taskRequest.comment.takeIf { it.isNotBlank() }?.also { this.comment = it }
                taskRequest.taskType.takeIf { it != TrackerWorkSide.NONE }?.also { this.taskType = it }
                taskRequest.visibility.takeIf { it != TrackerVisibility.NONE }?.also { this.visibility = it }
            }
            taskResponse = stub
        }
    }
}
