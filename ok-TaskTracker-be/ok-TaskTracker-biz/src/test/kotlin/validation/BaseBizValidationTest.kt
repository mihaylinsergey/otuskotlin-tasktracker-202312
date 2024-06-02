package validation

import TrackerCorSettings
import models.TrackerCommand
import ru.otus.otuskotlin.TrackerTask.biz.TrackerTaskProcessor

abstract class BaseBizValidationTest {
    protected abstract val command: TrackerCommand
    private val settings by lazy { TrackerCorSettings() }
    protected val processor by lazy { TrackerTaskProcessor(settings) }
}
