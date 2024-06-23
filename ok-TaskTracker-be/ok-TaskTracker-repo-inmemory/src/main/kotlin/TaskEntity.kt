import models.*

data class TaskEntity(
    val id: String? = null,
    val title: String? = null,
    val executor: String? = null,
    val stageList: List<TrackerStageList>? = null,
    val deadline: String? = null,
    val address: String? = null,
    val comment: String? = null,
    val photo: String? = null,
    val customerId: String? = null,
    val taskType: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: TrackerTask): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        executor = model.executor.takeIf { it.isNotBlank() },
        stageList = model.stageList.takeIf { it.isNotEmpty() },
        deadline = model.deadline.takeIf { it.isNotBlank() },
        address = model.address.takeIf { it.isNotBlank() },
        comment = model.comment.takeIf { it.isNotBlank() },
        photo = model.photo.takeIf { it.isNotBlank() },
        customerId = model.customerId.asString().takeIf { it.isNotBlank() },
        taskType = model.taskType.takeIf { it != TrackerWorkSide.NONE }?.name,
        visibility = model.visibility.takeIf { it != TrackerVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = TrackerTask(
        id = id?.let { TrackerTaskId(it) }?: TrackerTaskId.NONE,
        title = title?: "",
        executor = executor?: "",
        stageList = stageList?: mutableListOf(),
        deadline = deadline?: "",
        address = address?: "",
        comment = comment?: "",
        photo = photo?: "",
        customerId = customerId?.let { TrackerUserId(it) }?: TrackerUserId.NONE,
        taskType = taskType?.let { TrackerWorkSide.valueOf(it) }?: TrackerWorkSide.NONE,
        visibility = visibility?.let { TrackerVisibility.valueOf(it) }?: TrackerVisibility.NONE,
        lock = lock?.let { TrackerTaskLock(it) } ?: TrackerTaskLock.NONE,
    )
}
