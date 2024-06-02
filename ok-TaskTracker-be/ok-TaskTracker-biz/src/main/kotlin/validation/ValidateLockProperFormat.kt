package validation

import TrackerContext
import helpers.errorValidation
import helpers.fail
import models.TrackerTaskLock
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в TrackerTaskdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { taskValidating.lock != TrackerTaskLock.NONE && !taskValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = taskValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
