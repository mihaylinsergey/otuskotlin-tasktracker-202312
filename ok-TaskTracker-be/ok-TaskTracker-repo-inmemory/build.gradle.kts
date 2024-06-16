plugins {
    id("build-jvm")
}

dependencies {
    implementation(project(":ok-TaskTracker-common"))
    testImplementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-repo-common"))
    testImplementation(project(":ok-TaskTracker-repo-common"))
    implementation(project(":ok-TaskTracker-repo-stubs"))
    testImplementation(project(":ok-TaskTracker-repo-stubs"))
    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(project(":ok-TaskTracker-repo-tests"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
}