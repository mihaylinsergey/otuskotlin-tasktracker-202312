import models.*
import repo.DbTaskRequest
import repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class RepoTaskCreateTest {
    abstract val repo: TaskRepoInitialized
    protected open val lockNew = TrackerTaskLock("20000000-0000-0000-0000-000000000002")

    private val createObj = TrackerTask(
        title = "create object",
        executor = "create object executor",
        customerId = TrackerUserId("customer-123"),
        visibility = TrackerVisibility.VISIBLE_TO_GROUP,
        taskType = TrackerWorkSide.CUSTOMER
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTask(DbTaskRequest(createObj))
        println(result)
        val expected = createObj
        assertIs<DbTaskResponseOk>(result)
        assertNotEquals(TrackerTaskId.NONE, result.data.id)
        assertEquals(lockNew, result.data.lock)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.executor, result.data.executor)
        assertEquals(expected.taskType, result.data.taskType)
        assertNotEquals(TrackerTaskId.NONE, result.data.id)
    }

    companion object : BaseInitTasks("create") {
        override val initObjects: List<TrackerTask> = emptyList()
    }
}
