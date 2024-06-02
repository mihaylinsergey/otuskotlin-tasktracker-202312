package stubs

import TrackerContext
import TrackerCorSettings
import models.TrackerState
import ru.otus.otuskotlin.TaskTracker.logging.common.LogLevel
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.stubDeleteSuccess(title: String, corSettings: TrackerCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления задачи
    """.trimIndent()
    on { stubCase == TrackerStubs.SUCCESS && state == TrackerState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = TrackerState.FINISHING
            val stub = TrackerTaskStub.prepareResult {
                taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            taskResponse = stub
        }
    }
}
