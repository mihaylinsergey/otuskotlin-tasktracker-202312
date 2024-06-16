package repo

import TrackerContext
import helpers.fail
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == TrackerState.RUNNING }
    handle {
        val request = DbTaskFilterRequest(
            titleFilter = taskFilterValidated.searchString,
            customerId = taskFilterValidated.customerId,
            workSide = taskFilterValidated.workSide,
        )
        when(val result = taskRepo.searchTask(request)) {
            is DbTasksResponseOk -> tasksRepoDone = result.data.toMutableList()
            is DbTasksResponseErr -> fail(result.errors)
        }
    }
}
