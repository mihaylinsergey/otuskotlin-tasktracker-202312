package repo

import TrackerContext
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == TrackerState.RUNNING }
    handle {
        taskRepoPrepare = taskValidated.deepCopy()
        taskRepoPrepare.customerId = principal.id
    }
}
