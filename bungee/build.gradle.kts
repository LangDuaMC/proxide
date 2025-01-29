plugins {
  kotlin("jvm") version "2.1.0"
  id("com.ncorti.ktfmt.gradle") version "0.21.0"
}

group = "work.stdpi.proxide"
version = "1.0"

dependencies {
  implementation(project(":core"))
  compileOnly(project(":xcord"))
  compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
}

kotlin {
  jvmToolchain(21)
}

tasks.shadowJar {
  archiveBaseName.set("proxide-bungeecord")
}

ktfmt {
  kotlinLangStyle()
}
