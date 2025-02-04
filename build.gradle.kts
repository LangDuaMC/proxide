plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta6" apply false
    id("com.github.gmazzo.buildconfig") version "5.5.1"
    id("com.ncorti.ktfmt.gradle") version "0.21.0"
    kotlin("jvm") version "2.1.0" apply false
}

group = "work.stdpi.proxide"
version = "1.0"

subprojects {
    plugins.apply("java")
    plugins.apply("com.gradleup.shadow")
    plugins.apply("com.ncorti.ktfmt.gradle")
    plugins.apply("org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://libraries.minecraft.net/")
        maven("https://repo.md-5.net/content/repositories/snapshots/") // BungeeCord repo
    }

    tasks.named("build").configure {
        dependsOn("shadowJar")
    }
}

