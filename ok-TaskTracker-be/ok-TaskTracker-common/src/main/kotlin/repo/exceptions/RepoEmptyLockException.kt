package repo.exceptions

import models.TrackerTaskId

class RepoEmptyLockException(id: TrackerTaskId): RepoTaskException(
    id,
    "Lock is empty in DB"
)
