import utils.configureTests
import utils.projectVersion
import utils.publicationChannels

// Copyright 2021 Pants project contributors (see CONTRIBUTORS.md).
// Licensed under the Apache License, Version 2.0 (see LICENSE).

plugins {
    scala
    id("org.jetbrains.intellij") version "1.3.0"
}

group = "com.intellij.plugins"
version = projectVersion

allprojects {
    repositories {
        maven("https://www.jetbrains.com/intellij-repository/snapshots/")
        mavenCentral()
    }

    pluginManager.withPlugin("org.jetbrains.intellij") {
        val ideaVersion: String by project
        val scalaPluginVersion: String by project
        val pythonIdVersion: String by project
        intellij {
            type.set("IU")
            version.set(ideaVersion)
            plugins.set(listOf(
                    "com.intellij.properties",
                    "org.intellij.groovy",
                    "com.intellij.gradle",
                    "com.intellij.java",
                    "Pythonid:$pythonIdVersion",
                    "indexing-shared",
                    "org.intellij.scala:$scalaPluginVersion",
                    "JUnit")
            )
        }
    }
    tasks {
        withType<JavaCompile> {
            sourceCompatibility = "1.8"
            targetCompatibility = "1.8"
        }
    }
}

dependencies {
    val scalaVersion: String by project
    implementation(project(":common"))
    compileOnly("org.scala-lang:scala-library:$scalaVersion")

    testImplementation(project(":testFramework"))
    testCompileOnly("org.scala-lang:scala-library:$scalaVersion")
}

tasks {
    patchPluginXml {
        val pluginSinceBuild: String by project
        val pluginUntilBuild: String by project
        sinceBuild.set(pluginSinceBuild)
        untilBuild.set(pluginUntilBuild)
    }

    val separateTests by registering(Test::class) {
        // Those tests have to run separately due to the reuse of the project between tests
        group = "verification"

        configureTests()
        setForkEvery(1)

        isScanForTestClasses = false
        include("**/*PantsProjectCacheTest.class")
    }

    test {
        configureTests()

        doFirst {
            // For tests/com/twitter/intellij/pants/integration/WholeRepoIntegrationTest.java
            file(".cache/dummy_repo").takeIf { it.exists() }?.deleteRecursively()
            file("src/test/resources/testData/dummy_repo").copyRecursively(file(".cache/dummy_repo"), overwrite = true)
            file(".cache/dummy_repo/pants").setExecutable(true)

            // Remove IntelliJ index cache.
            file(".cache/intellij/*/idea-dist/system/caches/").takeIf { it.exists() }?.deleteRecursively()
        }
        isScanForTestClasses = false
        include("**/*Test.class")
        exclude("**/*PantsProjectCacheTest.class")
        finalizedBy(separateTests)
    }

    publishPlugin {
        channels.set(publicationChannels)
        System.getenv("TOKEN")?.takeIf { it.isNotBlank() }?.let {
            token.set(it)
        }
    }
}
