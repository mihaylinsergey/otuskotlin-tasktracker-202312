package ru.otus.otuskotlin.marketplace.common.helpers

import TrackerContext
import models.TrackerError
import models.TrackerState

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

inline fun TrackerContext.addError(vararg error: TrackerError) = errors.addAll(error)

inline fun TrackerContext.fail(error: TrackerError) {
    addError(error)
    state = TrackerState.FAILING
}
