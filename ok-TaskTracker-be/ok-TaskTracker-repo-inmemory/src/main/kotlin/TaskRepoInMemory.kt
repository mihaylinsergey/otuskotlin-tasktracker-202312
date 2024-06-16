import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.*
import repo.*
import repo.exceptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TaskRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : TaskRepoBase(), IRepoTask, IRepoTaskInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, TaskEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<TrackerTask>) = ads.map { task ->
        val entity = TaskEntity(task)
        require(entity.id != null)
        cache.put(entity.id, entity)
        task
    }

    override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse = tryTaskMethod {
        val key = randomUuid()
        val task = rq.task.copy(id = TrackerTaskId(key), lock = TrackerTaskLock(randomUuid()))
        val entity = TaskEntity(task)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbTaskResponseOk(task)
    }

    override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse = tryTaskMethod {
        val key = rq.id.takeIf { it != TrackerTaskId.NONE }?.asString() ?: return@tryTaskMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbTaskResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse = tryTaskMethod {
        val rqTask = rq.task
        val id = rqTask.id.takeIf { it != TrackerTaskId.NONE } ?: return@tryTaskMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqTask.lock.takeIf { it != TrackerTaskLock.NONE } ?: return@tryTaskMethod errorEmptyLock(id)

        mutex.withLock {
            val oldTask = cache.get(key)?.toInternal()
            when {
                oldTask == null -> errorNotFound(id)
                oldTask.lock == TrackerTaskLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldTask.lock != oldLock -> errorRepoConcurrency(oldTask, oldLock)
                else -> {
                    val newTask = rqTask.copy(lock = TrackerTaskLock(randomUuid()))
                    val entity = TaskEntity(newTask)
                    cache.put(key, entity)
                    DbTaskResponseOk(newTask)
                }
            }
        }
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse = tryTaskMethod {
        val id = rq.id.takeIf { it != TrackerTaskId.NONE } ?: return@tryTaskMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != TrackerTaskLock.NONE } ?: return@tryTaskMethod errorEmptyLock(id)

        mutex.withLock {
            val oldTask = cache.get(key)?.toInternal()
            when {
                oldTask == null -> errorNotFound(id)
                oldTask.lock == TrackerTaskLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldTask.lock != oldLock -> errorRepoConcurrency(oldTask, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbTaskResponseOk(oldTask)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse = tryTasksMethod {
        val result: List<TrackerTask> = cache.asMap().asSequence()
            .filter { entry ->
                rq.customerId.takeIf { it != TrackerUserId.NONE }?.let {
                    it.asString() == entry.value.customerId
                } ?: true
            }
            .filter { entry ->
                rq.workSide.takeIf { it != TrackerWorkSide.NONE }?.let {
                    it.name == entry.value.taskType
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbTasksResponseOk(result)
    }
}
