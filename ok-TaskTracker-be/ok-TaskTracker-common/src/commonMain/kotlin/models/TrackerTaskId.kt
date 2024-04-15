package models

import kotlin.jvm.JvmInline

@JvmInline
value class TrackerTaskId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TrackerTaskId("")
    }
}
