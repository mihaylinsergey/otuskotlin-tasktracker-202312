package plugins

import TaskRepoGremlin
import TaskRepoInMemory
import configs.ConfigPaths
import configs.GremlinConfig
import io.ktor.server.application.*
import repo.IRepoTask
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class TaskDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: TaskDbType): IRepoTask {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "arcade", "arcadedb", "graphdb", "gremlin", "g", "a" -> initGremliln()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'gremlin'"
        )
    }
}

fun Application.initInMemory(): IRepoTask {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return TaskRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

private fun Application.initGremliln(): IRepoTask {
    val config = GremlinConfig(environment.config)
    return TaskRepoGremlin(
        hosts = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
        enableSsl = config.enableSsl,
    )
}
