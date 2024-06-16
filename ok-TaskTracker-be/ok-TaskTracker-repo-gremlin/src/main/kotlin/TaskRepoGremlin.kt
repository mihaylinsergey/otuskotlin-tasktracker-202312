import TaskGremlinConst.FIELD_CUSTOMER_ID
import TaskGremlinConst.FIELD_LOCK
import TaskGremlinConst.FIELD_TASK_TYPE
import TaskGremlinConst.FIELD_TITLE
import TaskGremlinConst.RESULT_LOCK_FAILURE
import com.benasher44.uuid.uuid4
import mappers.*
import models.*
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import repo.*
import repo.exceptions.UnknownDbException
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr


class TaskRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "",
    private val graph: String = "graph", // Этот граф должен быть настроен в /home/arcadedb/config/gremlin-server.groovy
    val randomUuid: () -> String = { uuid4().toString() },
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
) : TaskRepoBase(), IRepoTask, IRepoTaskInitializable {

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster, graph)) }

    init {
        initRepo?.also { it(g) }
    }

    private fun save(task: TrackerTask): TrackerTask = g.addV(task.label())
        .addTrackerTask(task)
        .listTrackerTask()
        .next()
        ?.toTrackerTask()
        ?: throw RuntimeException("Cannot initialize object $task")

    override fun save(ads: Collection<TrackerTask>): Collection<TrackerTask> = ads.map { save(it) }

    override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse = tryTaskMethod {
        val task = rq.task.copy(lock = TrackerTaskLock(randomUuid()))
        g.addV(task.label())
            .addTrackerTask(task)
            .listTrackerTask()
            .next()
            ?.toTrackerTask()
            ?.let { DbTaskResponseOk(it) }
            ?: errorDb(UnknownDbException("Db object was not returned after creation by DB: $rq"))
    }

    private suspend fun <T: Any> checkNotFound(block: suspend () -> T?): T? = runCatching { block() }.getOrElse { e ->
        if (e.cause is ResponseException) null else throw e
    }

    override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse = tryTaskMethod {
        val key = rq.id.takeIf { it != TrackerTaskId.NONE }?.asString() ?: return@tryTaskMethod errorEmptyId
        val taskResp = checkNotFound { g.V(key).listTrackerTask().next()?.toTrackerTask() }
        when {
            taskResp == null -> errorNotFound(rq.id)
            else -> DbTaskResponseOk(taskResp)
        }
    }

    override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse = tryTaskMethod {
        val key = rq.task.id.takeIf { it != TrackerTaskId.NONE }?.asString() ?: return@tryTaskMethod errorEmptyId
        val oldLock = rq.task.lock.takeIf { it != TrackerTaskLock.NONE } ?: return@tryTaskMethod errorEmptyLock(rq.task.id)
        val newLock = TrackerTaskLock(randomUuid())
        val newAd = rq.task.copy(lock = newLock)
        val taskResp = checkNotFound {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Any>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a").addTrackerTask(newAd).listTrackerTask(),
                    gr.select<Vertex, Vertex>("a").listTrackerTask(result = RESULT_LOCK_FAILURE)
                )
                .next()
                ?.toTrackerTask()
        }
        when {
            taskResp == null -> errorNotFound(rq.task.id)
            taskResp.lock == newAd.lock -> DbTaskResponseOk(taskResp)
            else -> errorRepoConcurrency(taskResp, oldLock)
        }
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse = tryTaskMethod {
        val key = rq.id.takeIf { it != TrackerTaskId.NONE }?.asString() ?: return@tryTaskMethod errorEmptyId
        val oldLock = rq.lock.takeIf { it != TrackerTaskLock.NONE } ?: return@tryTaskMethod errorEmptyLock(rq.id)
        val res = checkNotFound {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Vertex>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>())
                        .listTrackerTask(),
                    gr.select<Vertex, Vertex>("a")
                        .listTrackerTask(result = RESULT_LOCK_FAILURE)
                )
                .next()
        }
        val taskResp = res?.toTrackerTask()
        when {
            taskResp == null -> errorNotFound(rq.id)
            res.getFailureMarker() == RESULT_LOCK_FAILURE -> errorRepoConcurrency(taskResp, oldLock)
            else -> DbTaskResponseOk(taskResp)
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse = tryTasksMethod {
        g.V()
            .apply { rq.customerId.takeIf { it != TrackerUserId.NONE }?.also { has(FIELD_CUSTOMER_ID, it.asString()) } }
            .apply { rq.workSide.takeIf { it != TrackerWorkSide.NONE }?.also { has(FIELD_TASK_TYPE, it.name) } }
            .apply {
                rq.titleFilter.takeIf { it.isNotBlank() }?.also { has(FIELD_TITLE, TextP.containing(it)) }
            }
            .listTrackerTask()
            .toList()
            .let { result -> DbTasksResponseOk(data = result.map { it.toTrackerTask() }) }
    }

}
