plugins {
    id("build-jvm")
}

dependencies {
    implementation(project(":ok-TaskTracker-common"))
    testImplementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-stubs"))
    implementation(libs.coroutines.core)
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(project(":ok-TaskTracker-repo-tests"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
}
