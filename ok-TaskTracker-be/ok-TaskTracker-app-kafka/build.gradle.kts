plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.TaskTracker.app.kafka.MainKt")
}

docker {
    javaApplication {
        baseImage.set("openjdk:17.0.2-slim")
    }
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-logging-logback")

    implementation(project(":ok-TaskTracker-app-common"))

    // transport models
    implementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-api-v1-jackson"))
    implementation(project(":ok-TaskTracker-api-v1-mappers"))
    // logic
    implementation(project(":ok-TaskTracker-biz"))

    testImplementation(kotlin("test-junit"))
}
