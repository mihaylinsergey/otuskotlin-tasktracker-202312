import models.TrackerTask
import models.TrackerTaskId
import models.TrackerWorkSide
import TrackerTaskStubRepair.TASK_CUSTOMER_REPAIR

object TrackerTaskStub {
    fun get(): TrackerTask = TASK_CUSTOMER_REPAIR.copy()

    fun prepareResult(block: TrackerTask.() -> Unit): TrackerTask = get().apply(block)

    fun prepareSearchList(filter: String, type: TrackerWorkSide) = listOf(
        tracerTaskCustomer("d-666-01", filter, type),
        tracerTaskCustomer("d-666-02", filter, type),
        tracerTaskCustomer("d-666-03", filter, type),
        tracerTaskCustomer("d-666-04", filter, type),
        tracerTaskCustomer("d-666-05", filter, type),
        tracerTaskCustomer("d-666-06", filter, type),
    )

    private fun tracerTaskCustomer(id: String, filter: String, type: TrackerWorkSide) =
        trackerTask(TASK_CUSTOMER_REPAIR, id = id, filter = filter, type = type)

    private fun trackerTaskExecutor(id: String, filter: String, type: TrackerWorkSide) =
        trackerTask(TASK_CUSTOMER_REPAIR, id = id, filter = filter, type = type)

    private fun trackerTask(base: TrackerTask, id: String, filter: String, type: TrackerWorkSide) = base.copy(
        id = TrackerTaskId(id),
        title = "$filter $id",
        comment = "desc $filter $id",
        taskType = type,
    )

}
