import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = TaskCreateResponse(
        task = TaskResponseObject(
            title = "task title",
            executor = "task executor",
            stageList = listOf(StageList(
                id = "1",
                stage = "stage one",
                value = "1.1",
                comment = "comment one"
            ),
                StageList(
                    id = "2",
                    stage = "stage two",
                    value = "2.1",
                    comment = "comment two"
                )),
            deadline = "01.01.2025",
            address = "some address",
            comment = "task comment",
            photo = "https://someurl",
            taskType = WorkSide.CUSTOMER,
            visibility = TaskVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as TaskCreateResponse

        assertEquals(response, obj)
    }
}
