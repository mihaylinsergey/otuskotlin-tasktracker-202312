package models

import kotlin.jvm.JvmInline

@JvmInline
value class TrackerUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = TrackerUserId("")
    }
}
