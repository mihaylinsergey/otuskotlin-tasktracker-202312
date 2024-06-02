package validation

import TrackerContext
import models.TrackerCommand
import models.TrackerState
import models.TrackerWorkMode
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationExecutorCorrect(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.get(),
    )
    processor.exec(ctx)
    println(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
    assertContains(ctx.taskValidated.executor, "Бригада №1")
}

fun validationExecutorTrim(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            executor = " \n\tabc \n\t"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(TrackerState.FAILING, ctx.state)
    assertEquals("abc", ctx.taskValidated.executor)
}

fun validationExecutorEmpty(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            executor = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("executor", error?.field)
    assertContains(error?.message ?: "", "executor")
}

fun validationExecutorSymbols(command: TrackerCommand, processor: TrackerTaskProcessor) = runBizTest {
    val ctx = TrackerContext(
        command = command,
        state = TrackerState.NONE,
        workMode = TrackerWorkMode.TEST,
        taskRequest = TrackerTaskStub.prepareResult {
            executor = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(TrackerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("executor", error?.field)
    assertContains(error?.message ?: "", "executor")
}
