import models.TrackerTask
import repo.IRepoTask


interface IRepoTaskInitializable: IRepoTask {
    fun save(ads: Collection<TrackerTask>): Collection<TrackerTask>
}
