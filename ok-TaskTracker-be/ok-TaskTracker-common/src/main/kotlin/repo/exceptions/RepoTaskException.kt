package repo.exceptions

import models.TrackerTaskId

open class RepoTaskException(
    @Suppress("unused")
    val taskId: TrackerTaskId,
    msg: String,
): RepoException(msg)
