package stubs

import TrackerContext
import models.TrackerError
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker
import helpers.fail

fun ICorChainDsl<TrackerContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора задачи
    """.trimIndent()
    on { stubCase == TrackerStubs.BAD_ID && state == TrackerState.RUNNING }
    handle {
        fail(
            TrackerError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
