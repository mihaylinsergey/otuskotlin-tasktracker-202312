package stubs

import TrackerContext
import models.TrackerError
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker
import helpers.fail

fun ICorChainDsl<TrackerContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для заголовка
    """.trimIndent()

    on { stubCase == TrackerStubs.BAD_TITLE && state == TrackerState.RUNNING }
    handle {
        fail(
            TrackerError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
