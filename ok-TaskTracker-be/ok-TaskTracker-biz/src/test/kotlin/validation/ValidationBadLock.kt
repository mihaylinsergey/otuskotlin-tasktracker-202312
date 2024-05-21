package validation

import TrackerContext
import models.TrackerCommand
import models.TrackerState
import models.TrackerTaskLock
import models.TrackerWorkMode
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
}

fun validationLockTrim(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            lock = TrackerTaskLock(" \n\t 123-234-abc-ABC \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
}

fun validationLockEmpty(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            lock = TrackerTaskLock("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            lock = TrackerTaskLock("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
