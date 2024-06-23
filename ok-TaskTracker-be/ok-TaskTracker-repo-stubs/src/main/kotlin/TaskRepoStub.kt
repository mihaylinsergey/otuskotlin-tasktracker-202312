import models.TrackerWorkSide
import repo.*

class TaskRepoStub() : IRepoTask {
    override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = TrackerTaskStub.get(),
        )
    }

    override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = TrackerTaskStub.get(),
        )
    }

    override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = TrackerTaskStub.get(),
        )
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = TrackerTaskStub.get(),
        )
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse {
        return DbTasksResponseOk(
            data = TrackerTaskStub.prepareSearchList(filter = "", TrackerWorkSide.CUSTOMER),
        )
    }
}
