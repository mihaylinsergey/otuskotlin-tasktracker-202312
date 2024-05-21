package validation

import TrackerContext
import models.TrackerState
import ru.otus.otuskotlin.TrackerTask.cor.ICorChainDsl
import ru.otus.otuskotlin.TrackerTask.cor.chain

fun ICorChainDsl<TrackerContext>.validation(block: ICorChainDsl<TrackerContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == TrackerState.RUNNING }
}
