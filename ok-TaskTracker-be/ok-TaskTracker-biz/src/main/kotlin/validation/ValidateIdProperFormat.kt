package validation

import TrackerContext
import helpers.errorValidation
import helpers.fail
import models.TrackerTaskId
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в TrackerAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { taskValidating.id != TrackerTaskId.NONE && ! taskValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = taskValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
