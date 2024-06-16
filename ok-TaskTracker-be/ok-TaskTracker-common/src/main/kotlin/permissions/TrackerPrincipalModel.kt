package permissions

import models.TrackerUserId

data class TrackerPrincipalModel(
    val id: TrackerUserId = TrackerUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<TrackerUserGroups> = emptySet()
) {
    fun genericName() = "$fname $mname $lname"
    companion object {
        val NONE = TrackerPrincipalModel()
    }
}
