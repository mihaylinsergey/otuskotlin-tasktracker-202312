package stubs

import TrackerContext
import models.TrackerError
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker
import helpers.fail

fun ICorChainDsl<TrackerContext>.stubValidationBadExecutor(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для исполнителя задачи
    """.trimIndent()
    on { stubCase == TrackerStubs.BAD_EXECUTOR && state == TrackerState.RUNNING }
    handle {
        fail(
            TrackerError(
                group = "validation",
                code = "validation-executor",
                field = "executor",
                message = "Wrong executor field"
            )
        )
    }
}
