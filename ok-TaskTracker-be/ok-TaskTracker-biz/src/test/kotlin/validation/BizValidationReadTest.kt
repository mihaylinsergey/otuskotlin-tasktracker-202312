package validation

import models.TrackerCommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = TrackerCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
