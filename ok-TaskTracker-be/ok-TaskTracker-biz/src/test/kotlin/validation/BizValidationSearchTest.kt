package validation

import TrackerContext
import kotlinx.coroutines.test.runTest
import models.TrackerCommand
import models.TrackerState
import models.TrackerTaskFilter
import models.TrackerWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = TrackerCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = TrackerContext(
            command = command,
            state = TrackerState.NONE,
            workMode = TrackerWorkMode.TEST,
            taskFilterRequest = TrackerTaskFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(TrackerState.FAILING, ctx.state)
    }
}
