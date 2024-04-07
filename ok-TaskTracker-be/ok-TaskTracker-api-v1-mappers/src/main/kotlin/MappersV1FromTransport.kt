import models.*
import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import stubs.TrackerStubs
import exceptions.UnknownRequestClass

fun TrackerContext.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTaskId() = this?.let { TrackerTaskId(it) } ?: TrackerTaskId.NONE
private fun String?.toTaskWithId() = TrackerTask(id = this.toTaskId())
private fun String?.toTaskLock() = this?.let { TrackerTaskLock(it) } ?: TrackerTaskLock.NONE

private fun TaskDebug?.transportToWorkMode(): TrackerWorkMode = when (this?.mode) {
    TaskRequestDebugMode.PROD -> TrackerWorkMode.PROD
    TaskRequestDebugMode.TEST -> TrackerWorkMode.TEST
    TaskRequestDebugMode.STUB -> TrackerWorkMode.STUB
    null -> TrackerWorkMode.PROD
}

private fun TaskDebug?.transportToStubCase(): TrackerStubs = when (this?.stub) {
    TaskRequestDebugStubs.SUCCESS -> TrackerStubs.SUCCESS
    TaskRequestDebugStubs.NOT_FOUND -> TrackerStubs.NOT_FOUND
    TaskRequestDebugStubs.BAD_ID -> TrackerStubs.BAD_ID
    TaskRequestDebugStubs.BAD_TITLE -> TrackerStubs.BAD_TITLE
    TaskRequestDebugStubs.BAD_DESCRIPTION -> TrackerStubs.BAD_DESCRIPTION
    TaskRequestDebugStubs.BAD_VISIBILITY -> TrackerStubs.BAD_VISIBILITY
    TaskRequestDebugStubs.CANNOT_DELETE -> TrackerStubs.CANNOT_DELETE
    TaskRequestDebugStubs.BAD_SEARCH_STRING -> TrackerStubs.BAD_SEARCH_STRING
    null -> TrackerStubs.NONE
}

fun TrackerContext.fromTransport(request: TaskCreateRequest) {
    command = TrackerCommand.CREATE
    taskRequest = request.task?.toInternal() ?: TrackerTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun TrackerContext.fromTransport(request: TaskReadRequest) {
    command = TrackerCommand.READ
    taskRequest = request.task.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskReadObject?.toInternal(): TrackerTask = if (this != null) {
    TrackerTask(id = id.toTaskId())
} else {
    TrackerTask()
}


fun TrackerContext.fromTransport(request: TaskUpdateRequest) {
    command = TrackerCommand.UPDATE
    taskRequest = request.task?.toInternal() ?: TrackerTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun TrackerContext.fromTransport(request: TaskDeleteRequest) {
    command = TrackerCommand.DELETE
    taskRequest = request.task.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskDeleteObject?.toInternal(): TrackerTask = if (this != null) {
    TrackerTask(
        id = id.toTaskId(),
        lock = lock.toTaskLock(),
    )
} else {
    TrackerTask()
}

fun TrackerContext.fromTransport(request: TaskSearchRequest) {
    command = TrackerCommand.SEARCH
    taskFilterRequest = request.taskFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskSearchFilter?.toInternal(): TrackerTaskFilter = TrackerTaskFilter(
    searchString = this?.searchString ?: ""
)

private fun TaskCreateObject.toInternal(): TrackerTask = TrackerTask(
    title = this.title ?: "",
    executor = this.executor ?: "",
    stageList = this.stageList?.toInternal() ?: emptyList(),
    deadline = this.deadline ?: "",
    address = this.address ?: "",
    comment = this.comment ?: "",
    photo = this.photo ?: "",
    taskType = this.taskType.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun List<StageList>.toInternal() : List<TrackerStageList>? = this
    .map { it.toInternal() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun StageList.toInternal() : TrackerStageList = TrackerStageList(
    id = this.id ?: "",
    stage = this.stage ?: "",
    value = this.value ?: "",
    comment = this.comment ?: "",
)

private fun TaskUpdateObject.toInternal(): TrackerTask = TrackerTask(
    id = this.id.toTaskId(),
    title = this.title ?: "",
    executor = this.executor ?: "",
    stageList = this.stageList?.toInternal() ?: emptyList(),
    deadline = this.deadline ?: "",
    address = this.address ?: "",
    comment = this.comment ?: "",
    photo = this.photo ?: "",
    taskType = this.taskType.fromTransport(),
    visibility = this.visibility.fromTransport(),
    lock = lock.toTaskLock(),
)

private fun TaskVisibility?.fromTransport(): TrackerVisibility = when (this) {
    TaskVisibility.PUBLIC -> TrackerVisibility.VISIBLE_PUBLIC
    TaskVisibility.CUSTOMER_ONLY -> TrackerVisibility.VISIBLE_TO_CUSTOMER
    TaskVisibility.REGISTERED_ONLY -> TrackerVisibility.VISIBLE_TO_GROUP
    null -> TrackerVisibility.NONE
}

private fun WorkSide?.fromTransport(): TrackerWorkSide = when (this) {
    WorkSide.CUSTOMER -> TrackerWorkSide.CUSTOMER
    WorkSide.EXECUTOR -> TrackerWorkSide.EXECUTOR
    null -> TrackerWorkSide.NONE
}

