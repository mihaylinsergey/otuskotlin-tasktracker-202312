import models.TrackerTask
import models.TrackerTaskId
import repo.DbTaskIdRequest
import repo.DbTaskResponseErr
import repo.DbTaskResponseErrWithData
import repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoTaskDeleteTest {
    abstract val repo: TaskRepoInitialized
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = TrackerTaskId("task-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteTask(DbTaskIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbTaskResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.executor, result.data.executor)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(notFoundId, lock = lockOld))

        assertIs<DbTaskResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteTask(DbTaskIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbTaskResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitTasks("delete") {
        override val initObjects: List<TrackerTask> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
