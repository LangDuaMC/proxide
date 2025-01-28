plugins {
    id("com.gradleup.shadow") version "9.0.0-beta6" apply false
}

group = "work.stdpi.proxide"
version = "1.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://libraries.minecraft.net/")
        maven("https://repo.md-5.net/content/repositories/snapshots/") // BungeeCord repo
    }
}