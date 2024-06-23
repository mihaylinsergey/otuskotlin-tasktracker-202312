package repo

import helpers.errorSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class TaskRepoBase: IRepoTask {

    protected suspend fun tryTaskMethod(timeout: Duration = 10.seconds, ctx: CoroutineContext = Dispatchers.IO, block: suspend () -> IDbTaskResponse) = try {
        withTimeout(timeout) {
            withContext(ctx) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbTaskResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryTasksMethod(block: suspend () -> IDbTasksResponse) = try {
        block()
    } catch (e: Throwable) {
        DbTasksResponseErr(errorSystem("methodException", e = e))
    }

}
