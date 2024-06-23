import models.TrackerError
import models.TrackerTask
import models.TrackerTaskId
import repo.DbTaskIdRequest
import repo.DbTaskResponseErr
import repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTaskReadTest {
    abstract val repo: TaskRepoInitialized
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(readSucc.id))

        assertIs<DbTaskResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readTask(DbTaskIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbTaskResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: TrackerError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTasks("read") {
        override val initObjects: List<TrackerTask> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = TrackerTaskId("task-repo-read-notFound")

    }
}
