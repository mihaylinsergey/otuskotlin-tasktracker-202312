import models.TrackerTask
import models.TrackerTaskId
import models.TrackerTaskLock
import ru.otus.otuskotlin.TaskTracker.api.v1.models.TaskCreateObject
import ru.otus.otuskotlin.TaskTracker.api.v1.models.TaskDeleteObject
import ru.otus.otuskotlin.TaskTracker.api.v1.models.TaskReadObject
import ru.otus.otuskotlin.TaskTracker.api.v1.models.TaskUpdateObject

fun TrackerTask.toTransportCreate() = TaskCreateObject(
    title = title.takeIf { it.isNotBlank() },
    executor = executor.takeIf { it.isNotBlank() },
    stageList = stageList.stageListToTransport(),
    deadline = deadline.takeIf { it.isNotBlank() },
    address = address.takeIf { it.isNotBlank() },
    comment = comment.takeIf { it.isNotBlank() },
    photo = photo.takeIf { it.isNotBlank() },
    taskType = taskType.toTransportTask(),
    visibility = visibility.toTransportTask(),
)

fun TrackerTask.toTransportRead() = TaskReadObject(
    id = id.takeIf { it != TrackerTaskId.NONE }?.asString(),
)

fun TrackerTask.toTransportUpdate() = TaskUpdateObject(
    id = id.takeIf { it != TrackerTaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    executor = executor.takeIf { it.isNotBlank() },
    stageList = stageList.stageListToTransport(),
    deadline = deadline.takeIf { it.isNotBlank() },
    address = address.takeIf { it.isNotBlank() },
    comment = comment.takeIf { it.isNotBlank() },
    photo = photo.takeIf { it.isNotBlank() },
    taskType = taskType.toTransportTask(),
    visibility = visibility.toTransportTask(),
    lock = lock.takeIf { it != TrackerTaskLock.NONE }?.asString(),
)

fun TrackerTask.toTransportDelete() = TaskDeleteObject(
    id = id.takeIf { it != TrackerTaskId.NONE }?.asString(),
    lock = lock.takeIf { it != TrackerTaskLock.NONE }?.asString(),
)
