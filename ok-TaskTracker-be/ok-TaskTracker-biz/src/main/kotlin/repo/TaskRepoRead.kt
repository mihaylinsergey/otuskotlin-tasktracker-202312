package repo

import TrackerContext
import helpers.fail
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == TrackerState.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskValidated)
        when(val result = taskRepo.readTask(request)) {
            is DbTaskResponseOk -> taskRepoRead = result.data
            is DbTaskResponseErr -> fail(result.errors)
            is DbTaskResponseErrWithData -> {
                fail(result.errors)
                taskRepoRead = result.data
            }
        }
    }
}
