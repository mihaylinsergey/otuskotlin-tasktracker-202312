package stubs

import TrackerContext
import models.TrackerError
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker
import helpers.fail

fun ICorChainDsl<TrackerContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == TrackerStubs.DB_ERROR && state == TrackerState.RUNNING }
    handle {
        fail(
            TrackerError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
