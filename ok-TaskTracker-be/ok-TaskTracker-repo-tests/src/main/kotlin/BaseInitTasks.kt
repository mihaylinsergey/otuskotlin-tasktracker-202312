import models.*

abstract class BaseInitTasks(private val op: String): IInitObjects<TrackerTask> {
    open val lockOld: TrackerTaskLock = TrackerTaskLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: TrackerTaskLock = TrackerTaskLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        customerId: TrackerUserId = TrackerUserId("owner-123"),
        taskType: TrackerWorkSide = TrackerWorkSide.CUSTOMER,
        lock: TrackerTaskLock = lockOld,
    ) = TrackerTask(
        id = TrackerTaskId("task-repo-$op-$suf"),
        title = "$suf stub",
        executor = "$suf stub executor",
        customerId = customerId,
        visibility = TrackerVisibility.VISIBLE_TO_CUSTOMER,
        taskType = taskType,
        lock = lock,
    )
}
