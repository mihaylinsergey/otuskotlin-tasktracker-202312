plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))

    implementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-stubs"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-cor")
    implementation("ru.otus.otuskotlin.TaskTracker.libs:ok-TaskTracker-lib-logging-common")

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    api(libs.coroutines.test)
    testImplementation(kotlin("test-junit"))
    // https://mavenlibs.com/maven/dependency/org.jetbrains.kotlinx/kotlinx-datetime
    testImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}
