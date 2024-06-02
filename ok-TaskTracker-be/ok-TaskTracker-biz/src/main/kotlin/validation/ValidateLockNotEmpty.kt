package validation

import TrackerContext
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
