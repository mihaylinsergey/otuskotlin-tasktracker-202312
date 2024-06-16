package repo

import models.TrackerTask
import models.TrackerTaskId
import models.TrackerTaskLock

data class DbTaskIdRequest(
    val id: TrackerTaskId,
    val lock: TrackerTaskLock = TrackerTaskLock.NONE,
) {
    constructor(ad: TrackerTask): this(ad.id, ad.lock)
}
