plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.TaskTracker.app.ktor.main")
}

docker {
    javaApplication {
        baseImage.set("openjdk:17.0.2-slim")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // jackson
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.calllogging)
    implementation(libs.ktor.server.headers.default)

    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.yaml)

    implementation(libs.logback)

    // transport models
    implementation(project(":ok-TaskTracker-api-v1-jackson"))
    implementation(project(":ok-TaskTracker-api-v1-mappers"))

    implementation("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-logging-logback")
    implementation("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-logging-common")

    implementation(project(":ok-TaskTracker-app-common"))
    implementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-biz"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))

    testImplementation(libs.ktor.server.test)
    testImplementation(libs.ktor.client.negotiation)
}