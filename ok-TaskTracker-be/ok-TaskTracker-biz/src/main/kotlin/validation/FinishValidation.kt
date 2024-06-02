package validation

import TrackerContext
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.worker

fun ICorChainDsl<TrackerContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == TrackerState.RUNNING }
    handle {
        taskValidated = taskValidating
    }
}

fun ICorChainDsl<TrackerContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == TrackerState.RUNNING }
    handle {
        taskFilterValidated = taskFilterValidating
    }
}
