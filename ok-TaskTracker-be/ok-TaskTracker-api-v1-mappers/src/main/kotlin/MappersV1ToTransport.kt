import exceptions.UnknownTrackerCommand
import models.*
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*

fun TrackerContext.toTransportTask(): IResponse = when (val cmd = command) {
    TrackerCommand.CREATE -> toTransportCreate()
    TrackerCommand.READ -> toTransportRead()
    TrackerCommand.UPDATE -> toTransportUpdate()
    TrackerCommand.DELETE -> toTransportDelete()
    TrackerCommand.SEARCH -> toTransportSearch()
    TrackerCommand.INIT -> toTransportInit()
    TrackerCommand.FINISH -> object: IResponse {
        override val responseType: String? = null
        override val result: ResponseResult? = null
        override val errors: List<Error>? = null
    }
    TrackerCommand.NONE -> throw UnknownTrackerCommand(cmd)
}

fun TrackerContext.toTransportCreate() = TaskCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun TrackerContext.toTransportRead() = TaskReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun TrackerContext.toTransportUpdate() = TaskUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun TrackerContext.toTransportDelete() = TaskDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun TrackerContext.toTransportSearch() = TaskSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    tasks = tasksResponse.toTransportTask()
)

fun TrackerContext.toTransportInit() = TaskInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<TrackerTask>.toTransportTask(): List<TaskResponseObject>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun TrackerTask.toTransportTask(): TaskResponseObject = TaskResponseObject(
    id = id.takeIf { it != TrackerTaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    executor = executor.takeIf { it.isNotBlank() },
    stageList = stageList.stageListToTransport(),
    deadline = deadline.takeIf { it.isNotBlank() },
    address = address.takeIf { it.isNotBlank() },
    comment = comment.takeIf { it.isNotBlank() },
    photo = photo.takeIf { it.isNotBlank() },
    customerId = customerId.takeIf { it != TrackerUserId.NONE }?.asString(),
    taskType = taskType.toTransportTask(),
    visibility = visibility.toTransportTask(),
    permissions = permissionsClient.toTransportTask(),
)

internal fun List<TrackerStageList>.stageListToTransport() : List<StageList>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun TrackerStageList.toTransportTask() : StageList = StageList(
    id = this.id,
    stage = this.stage,
    value = this.value,
    comment = this.comment,
)

private fun Set<TrackerTaskPermissionClient>.toTransportTask(): Set<TaskPermissions>? = this
    .map { it.toTransportTask() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun TrackerTaskPermissionClient.toTransportTask() = when (this) {
    TrackerTaskPermissionClient.READ -> TaskPermissions.READ
    TrackerTaskPermissionClient.UPDATE -> TaskPermissions.UPDATE
    TrackerTaskPermissionClient.MAKE_VISIBLE_OWNER -> TaskPermissions.MAKE_VISIBLE_OWN
    TrackerTaskPermissionClient.MAKE_VISIBLE_GROUP -> TaskPermissions.MAKE_VISIBLE_GROUP
    TrackerTaskPermissionClient.MAKE_VISIBLE_PUBLIC -> TaskPermissions.MAKE_VISIBLE_PUBLIC
    TrackerTaskPermissionClient.DELETE -> TaskPermissions.DELETE
}

internal fun TrackerVisibility.toTransportTask(): TaskVisibility? = when (this) {
    TrackerVisibility.VISIBLE_PUBLIC -> TaskVisibility.PUBLIC
    TrackerVisibility.VISIBLE_TO_GROUP -> TaskVisibility.REGISTERED_ONLY
    TrackerVisibility.VISIBLE_TO_CUSTOMER -> TaskVisibility.CUSTOMER_ONLY
    TrackerVisibility.NONE -> null
}

internal fun TrackerWorkSide.toTransportTask(): WorkSide? = when (this) {
    TrackerWorkSide.CUSTOMER -> WorkSide.CUSTOMER
    TrackerWorkSide.EXECUTOR -> WorkSide.EXECUTOR
    TrackerWorkSide.NONE -> null
}

internal fun List<TrackerError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun TrackerError.toTransportTask() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun TrackerState.toResult(): ResponseResult? = when (this) {
    TrackerState.RUNNING -> ResponseResult.SUCCESS
    TrackerState.FAILING -> ResponseResult.ERROR
    TrackerState.FINISHING -> ResponseResult.SUCCESS
    TrackerState.NONE -> null
}
