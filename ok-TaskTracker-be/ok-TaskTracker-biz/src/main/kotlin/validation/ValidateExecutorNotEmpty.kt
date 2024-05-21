package validation

import TrackerContext
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.validateExecutorNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.executor.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "executor",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
