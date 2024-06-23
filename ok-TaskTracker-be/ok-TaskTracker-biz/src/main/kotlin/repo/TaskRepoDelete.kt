package repo

import TrackerContext
import helpers.fail
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление задачи из БД по ID"
    on { state == TrackerState.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskRepoPrepare)
        when(val result = taskRepo.deleteTask(request)) {
            is DbTaskResponseOk -> taskRepoDone = result.data
            is DbTaskResponseErr -> {
                fail(result.errors)
                taskRepoDone = taskRepoRead
            }
            is DbTaskResponseErrWithData -> {
                fail(result.errors)
                taskRepoDone = result.data
            }
        }
    }
}
