package validation

import TrackerContext
import models.TrackerCommand
import models.TrackerState
import models.TrackerTaskId
import models.TrackerWorkMode
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
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

fun validationIdTrim(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            id = TrackerTaskId(" \n\t ${id.asString()} \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
}

fun validationIdEmpty(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            id = TrackerTaskId("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            id = TrackerTaskId("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    println(ctx.errors)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
