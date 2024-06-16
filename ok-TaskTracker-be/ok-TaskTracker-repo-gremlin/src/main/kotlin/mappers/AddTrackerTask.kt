package mappers

import TaskGremlinConst.FIELD_CUSTOMER_ID
import TaskGremlinConst.FIELD_EXECUTOR
import TaskGremlinConst.FIELD_ID
import TaskGremlinConst.FIELD_LOCK
import TaskGremlinConst.FIELD_TASK_TYPE
import TaskGremlinConst.FIELD_TITLE
import TaskGremlinConst.FIELD_TMP_RESULT
import TaskGremlinConst.FIELD_VISIBILITY
import TaskGremlinConst.RESULT_SUCCESS
import models.*
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import exceptions.WrongEnumException


fun GraphTraversal<Vertex, Vertex>.addTrackerTask(task: TrackerTask): GraphTraversal<Vertex, Vertex> =
    this
        .property(VertexProperty.Cardinality.single, FIELD_TITLE, task.title.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.single, FIELD_EXECUTOR, task.executor.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, task.lock.takeIf { it != TrackerTaskLock.NONE }?.asString())
        .property(
            VertexProperty.Cardinality.single,
            FIELD_CUSTOMER_ID,
            task.customerId.asString().takeIf { it.isNotBlank() }) // здесь можно сделать ссылку на объект владельца
        .property(VertexProperty.Cardinality.single, FIELD_TASK_TYPE, task.taskType.takeIf { it != TrackerWorkSide.NONE }?.name)
        .property(
            VertexProperty.Cardinality.single,
            FIELD_VISIBILITY,
            task.visibility.takeIf { it != TrackerVisibility.NONE }?.name
        )

fun GraphTraversal<Vertex, Vertex>.listTrackerTask(result: String = RESULT_SUCCESS): GraphTraversal<Vertex, MutableMap<String, Any>> =
    project<Any?>(
        FIELD_ID,
        FIELD_CUSTOMER_ID,
        FIELD_LOCK,
        FIELD_TITLE,
        FIELD_EXECUTOR,
        FIELD_TASK_TYPE,
        FIELD_VISIBILITY,
        FIELD_TMP_RESULT,
    )
        .by(gr.id<Vertex>())
        .by(FIELD_CUSTOMER_ID)
        .by(FIELD_LOCK)
        .by(FIELD_TITLE)
        .by(FIELD_EXECUTOR)
        .by(FIELD_TASK_TYPE)
        .by(FIELD_VISIBILITY)
        .by(gr.constant(result))
//        .by(elementMap<Vertex, Map<Any?, Any?>>())

fun Map<String, Any?>.toTrackerTask(): TrackerTask = TrackerTask(
    id = (this[FIELD_ID] as? String)?.let { TrackerTaskId(it) } ?: TrackerTaskId.NONE,
    customerId = (this[FIELD_CUSTOMER_ID] as? String)?.let { TrackerUserId(it) } ?: TrackerUserId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { TrackerTaskLock(it) } ?: TrackerTaskLock.NONE,
    title = (this[FIELD_TITLE] as? String) ?: "",
    executor = (this[FIELD_EXECUTOR] as? String) ?: "",
    taskType = when (val value = this[FIELD_TASK_TYPE] as? String) {
        TrackerWorkSide.CUSTOMER.name -> TrackerWorkSide.CUSTOMER
        TrackerWorkSide.EXECUTOR.name -> TrackerWorkSide.EXECUTOR
        null -> TrackerWorkSide.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "taskType = '$value' cannot be converted to ${TrackerWorkSide::class}"
        )
    },
    visibility = when (val value = this[FIELD_VISIBILITY]) {
        TrackerVisibility.VISIBLE_PUBLIC.name -> TrackerVisibility.VISIBLE_PUBLIC
        TrackerVisibility.VISIBLE_TO_GROUP.name -> TrackerVisibility.VISIBLE_TO_GROUP
        TrackerVisibility.VISIBLE_TO_CUSTOMER.name -> TrackerVisibility.VISIBLE_TO_CUSTOMER
        null -> TrackerVisibility.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "visibility = '$value' cannot be converted to ${TrackerVisibility::class}"
        )
    },
)

fun Map<String, Any?>.getFailureMarker(): String? = this[FIELD_TMP_RESULT] as? String
