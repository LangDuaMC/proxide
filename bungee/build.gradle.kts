plugins {
    kotlin("jvm")
    id("com.ncorti.ktfmt.gradle")
}

group = "work.stdpi.proxide"
version = "1.0.2"

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    compileOnly(project(":xcord"))
    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}

tasks.shadowJar {
    archiveBaseName.set("proxide-bungeecord")
}

ktfmt {
    kotlinLangStyle()
}
