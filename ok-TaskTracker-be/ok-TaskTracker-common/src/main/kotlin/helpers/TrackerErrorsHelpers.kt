package helpers

import TrackerContext
import models.TrackerError
import models.TrackerState
import ru.otus.otuskotlin.TaskTracker.logging.common.LogLevel

fun Throwable.asTrackerError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = TrackerError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun TrackerContext.addError(error: TrackerError) = errors.add(error)
inline fun TrackerContext.addErrors(error: Collection<TrackerError>) = errors.addAll(error)

inline fun TrackerContext.fail(error: TrackerError) {
    addError(error)
    state = TrackerState.FAILING
}

inline fun TrackerContext.fail(errors: Collection<TrackerError>) {
    addErrors(errors)
    state = TrackerState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = TrackerError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = TrackerError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
