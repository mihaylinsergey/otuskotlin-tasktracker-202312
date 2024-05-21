package validation

import TrackerContext
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<TrackerContext>.validateExecutorHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { taskValidating.executor.isNotEmpty() && !taskValidating.executor.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "executor",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
