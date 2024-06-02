package stubs

import TrackerContext
import TrackerCorSettings
import models.TrackerState
import models.TrackerWorkSide
import ru.otus.otuskotlin.TaskTracker.logging.common.LogLevel
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.stubSearchSuccess(title: String, corSettings: TrackerCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска задач
    """.trimIndent()
    on { stubCase == TrackerStubs.SUCCESS && state == TrackerState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = TrackerState.FINISHING
            tasksResponse.addAll(TrackerTaskStub
                .prepareSearchList(taskFilterRequest.searchString, TrackerWorkSide.CUSTOMER))
        }
    }
}
