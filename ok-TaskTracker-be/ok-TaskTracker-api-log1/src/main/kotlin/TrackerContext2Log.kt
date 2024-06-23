import models.*
import ru.otus.otuskotlin.TaskTracker.api.log1.models.*
import java.time.Clock

fun TrackerContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.systemUTC().toString(),
    logId = logId,
    source = "ok-Tracker",
    task = toTrackerLog(),
    errors = errors.map { it.toLog() },
)

private fun TrackerContext.toTrackerLog(): TrackerLogModel? {
    val taskNone = TrackerTask()
    return TrackerLogModel(
        requestId = requestId.takeIf { it != TrackerRequestId.NONE }?.asString(),
        requestTask = taskRequest.takeIf { it != taskNone }?.toLog(),
        responseTask = taskResponse.takeIf { it != taskNone }?.toLog(),
        responseTasks = tasksResponse.takeIf { it.isNotEmpty() }?.filter { it != taskNone }?.map { it.toLog() },
        requestFilter = taskFilterRequest.takeIf { it != TrackerTaskFilter() }?.toLog(),
    ).takeIf { it != TrackerLogModel() }
}

private fun TrackerTaskFilter.toLog() = TaskFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    customerId = customerId.takeIf { it != TrackerUserId.NONE }?.asString(),
    workSide = workSide.takeIf { it != TrackerWorkSide.NONE }?.name,
)

private fun TrackerError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
//    level = level.name,
)

private fun TrackerTask.toLog() = TaskLog(
    id = id.takeIf { it != TrackerTaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    executor = executor.takeIf { it.isNotBlank() },
    taskType = taskType.takeIf { it != TrackerWorkSide.NONE }?.name,
    visibility = visibility.takeIf { it != TrackerVisibility.NONE }?.name,
    customerId = customerId.takeIf { it != TrackerUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
