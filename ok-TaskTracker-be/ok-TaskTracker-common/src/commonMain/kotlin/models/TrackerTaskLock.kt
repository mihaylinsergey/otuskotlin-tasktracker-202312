package models

import kotlin.jvm.JvmInline

@JvmInline
value class TrackerTaskLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TrackerTaskLock("")
    }
}
