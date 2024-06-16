package repo

import models.TrackerError
import models.TrackerTask

sealed interface IDbTasksResponse: IDbResponse<List<TrackerTask>>

data class DbTasksResponseOk(
    val data: List<TrackerTask>
): IDbTasksResponse

@Suppress("unused")
data class DbTasksResponseErr(
    val errors: List<TrackerError> = emptyList()
): IDbTasksResponse {
    constructor(err: TrackerError): this(listOf(err))
}
