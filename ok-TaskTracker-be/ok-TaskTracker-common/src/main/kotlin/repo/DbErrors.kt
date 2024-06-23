package repo

import helpers.errorSystem
import models.TrackerError
import models.TrackerTask
import models.TrackerTaskId
import models.TrackerTaskLock
import repo.exceptions.RepoConcurrencyException
import repo.exceptions.RepoException


const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: TrackerTaskId) = DbTaskResponseErr(
    TrackerError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbTaskResponseErr(
    TrackerError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldTask: TrackerTask,
    expectedLock: TrackerTaskLock,
    exception: Exception = RepoConcurrencyException(
        id = oldTask.id,
        expectedLock = expectedLock,
        actualLock = oldTask.lock,
    ),
) = DbTaskResponseErrWithData(
    task = oldTask,
    err = TrackerError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldTask.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: TrackerTaskId) = DbTaskResponseErr(
    TrackerError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Task ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbTaskResponseErr(
    errorSystem(
        violationCode = "db-error",
        e = e
    )
)
