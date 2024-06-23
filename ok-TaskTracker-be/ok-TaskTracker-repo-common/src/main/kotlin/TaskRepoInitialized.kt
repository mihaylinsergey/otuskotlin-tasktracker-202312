import models.TrackerTask


/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class TaskRepoInitialized(
    val repo: IRepoTaskInitializable,
    initObjects: Collection<TrackerTask> = emptyList(),
) : IRepoTaskInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<TrackerTask> = save(initObjects).toList()
}
