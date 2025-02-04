val kotlin_version: String by project
// val logback_version: String by project
val prometheus_version: String by project

plugins {
  kotlin("jvm") version "2.1.0"
  id("io.ktor.plugin") version "3.0.3"
  id("com.ncorti.ktfmt.gradle") version "0.21.0"
    id("com.github.gmazzo.buildconfig")
}

repositories { mavenCentral() }

group = "work.stdpi.proxide"
version = "1.0.2"

dependencies {
  implementation("io.ktor:ktor-server-core")
  implementation("io.ktor:ktor-server-metrics-micrometer")
  implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
  implementation("io.ktor:ktor-server-netty")
  implementation("io.ktor:ktor-server-compression")
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("com.maxmind.geoip2:geoip2:4.2.1")
  // https://mvnrepository.com/artifact/org.apache.commons/commons-compress
  implementation("org.apache.commons:commons-compress:1.27.1")
  // implementation("ch.qos.logback:logback-classic:$logback_version")
  testImplementation("io.ktor:ktor-server-test-host")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.named("shadowJar") { enabled = false }

ktfmt {
  kotlinLangStyle()
}

tasks.shadowJar {
    enabled = false
}

tasks.generateBuildConfig {
    enabled = true
}

buildConfig {
    className("BuildMetadata")
    packageName("work.stdpi.proxide")

    buildConfigField("String", "VERSION", "\"${project.version}\"")
    useJavaOutput()
    useKotlinOutput()
    useKotlinOutput {
        topLevelConstants = true
    }
    useKotlinOutput { internalVisibility = false }
}
