package repo

import models.TrackerError
import models.TrackerTask

sealed interface IDbTaskResponse: IDbResponse<TrackerTask>

data class DbTaskResponseOk(
    val data: TrackerTask
): IDbTaskResponse

data class DbTaskResponseErr(
    val errors: List<TrackerError> = emptyList()
): IDbTaskResponse {
    constructor(err: TrackerError): this(listOf(err))
}

data class DbTaskResponseErrWithData(
    val data: TrackerTask,
    val errors: List<TrackerError> = emptyList()
): IDbTaskResponse {
    constructor(task: TrackerTask, err: TrackerError): this(task, listOf(err))
}
