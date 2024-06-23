import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated

plugins {
    id("build-jvm")
}

val generatedPath = layout.buildDirectory.dir("generated/main/kotlin").get()
sourceSets {
    main {
        java.srcDir(generatedPath)
    }
}

dependencies {
    implementation(project(":ok-TaskTracker-common"))
    implementation(project(":ok-TaskTracker-repo-common"))

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.gdb.gremlin.driver)
    implementation(libs.gdb.arcade.engine)
    implementation(libs.gdb.arcade.network)
    implementation(libs.gdb.arcade.gremlin)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.testcontainers.core)
    testImplementation(project(":ok-TaskTracker-repo-tests"))
}

val arcadeDbVersion: String by project

tasks {
    val gradleConstants by creating {
        file("$generatedPath/GradleConstants.kt").apply {
            ensureParentDirsCreated()
            writeText(
                """
                    package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

                    const val ARCADEDB_VERSION = "${libs.versions.arcadedb.get()}"
                """.trimIndent()
            )
        }
    }
    compileKotlin.get().dependsOn(gradleConstants)
}
