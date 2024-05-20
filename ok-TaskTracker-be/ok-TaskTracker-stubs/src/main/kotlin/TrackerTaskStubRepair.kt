import models.*

object TrackerTaskStubRepair {
    val TASK_CUSTOMER_REPAIR: TrackerTask
        get() = TrackerTask(
            id = TrackerTaskId("123"),
            title = "Ремонт дорожного покрытия",
            executor = "Бригада №1",
            stageList = mutableListOf(TrackerStageList(
                id = "1",
                stage = "Удаление асфальтового покрытия",
                value = "3 м2",
                comment = "Организовать вывоз мустора"),
                TrackerStageList(
                    id = "2",
                    stage = "Восстановление дорожного покрытия",
                    value = "3 м2",
                    comment = "Без комментариев")),
            deadline = "",
            address = "",
            comment = "",
            photo = "",
            customerId = TrackerUserId("1"),
            taskType = TrackerWorkSide.CUSTOMER,
            visibility = TrackerVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                TrackerTaskPermissionClient.READ,
                TrackerTaskPermissionClient.UPDATE,
                TrackerTaskPermissionClient.DELETE,
                TrackerTaskPermissionClient.MAKE_VISIBLE_PUBLIC,
                TrackerTaskPermissionClient.MAKE_VISIBLE_GROUP,
                TrackerTaskPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val TASK_EXECUTOR_REPAIR = TASK_CUSTOMER_REPAIR.copy(taskType = TrackerWorkSide.EXECUTOR)
}
