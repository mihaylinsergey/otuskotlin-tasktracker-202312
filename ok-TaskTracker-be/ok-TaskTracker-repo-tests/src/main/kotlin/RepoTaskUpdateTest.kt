import models.*
import repo.DbTaskRequest
import repo.DbTaskResponseErr
import repo.DbTaskResponseErrWithData
import repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTaskUpdateTest {
    abstract val repo: TaskRepoInitialized
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = TrackerTaskId("task-repo-update-not-found")
    protected val lockBad = TrackerTaskLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = TrackerTaskLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        TrackerTask(
            id = updateSucc.id,
            title = "update object",
            executor = "update object executor",
            customerId = TrackerUserId("customer-123"),
            visibility = TrackerVisibility.VISIBLE_TO_GROUP,
            taskType = TrackerWorkSide.CUSTOMER,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = TrackerTask(
        id = updateIdNotFound,
        title = "update object not found",
        executor = "update object not found executor",
        customerId = TrackerUserId("owner-123"),
        visibility = TrackerVisibility.VISIBLE_TO_GROUP,
        taskType = TrackerWorkSide.CUSTOMER,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        TrackerTask(
            id = updateConc.id,
            title = "update object not found",
            executor = "update object not found executor",
            customerId = TrackerUserId("owner-123"),
            visibility = TrackerVisibility.VISIBLE_TO_GROUP,
            taskType = TrackerWorkSide.CUSTOMER,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbTaskResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbTaskResponseErrWithData)?.errors}")
        assertIs<DbTaskResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.executor, result.data.executor)
        assertEquals(reqUpdateSucc.taskType, result.data.taskType)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateNotFound))
        assertIs<DbTaskResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateConc))
        assertIs<DbTaskResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitTasks("update") {
        override val initObjects: List<TrackerTask> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
