plugins {
    id("build-jvm")
}

dependencies {
    implementation(project(":ok-TaskTracker-common"))
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(libs.coroutines.core)
    testImplementation(libs.coroutines.core)
    implementation(libs.coroutines.test)
    testImplementation(libs.coroutines.test)
    testImplementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-repo-stubs"))
    implementation(project(":ok-TaskTracker-repo-common"))
    implementation(project(":ok-TaskTracker-stubs"))
    testImplementation(project(":ok-TaskTracker-stubs"))
    implementation(kotlin("test-junit"))
    testImplementation(kotlin("test-junit"))
}
