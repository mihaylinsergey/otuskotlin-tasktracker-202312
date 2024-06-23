package repo

import TrackerContext
import helpers.fail
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление задачи в БД"
    on { state == TrackerState.RUNNING }
    handle {
        val request = DbTaskRequest(taskRepoPrepare)
        when(val result = taskRepo.createTask(request)) {
            is DbTaskResponseOk -> taskRepoDone = result.data
            is DbTaskResponseErr -> fail(result.errors)
            is DbTaskResponseErrWithData -> fail(result.errors)
        }
    }
}
