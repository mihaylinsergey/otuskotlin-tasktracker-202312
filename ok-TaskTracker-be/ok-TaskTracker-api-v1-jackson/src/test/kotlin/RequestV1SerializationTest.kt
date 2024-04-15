import ru.otus.otuskotlin.TaskTracker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = TaskCreateRequest(
        debug = TaskDebug(
            mode = TaskRequestDebugMode.STUB,
            stub = TaskRequestDebugStubs.BAD_TITLE
        ),
        task = TaskCreateObject(
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
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as TaskCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"task": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, TaskCreateRequest::class.java)

        assertEquals(null, obj.task)
    }
}
