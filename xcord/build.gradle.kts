group = "me.minebuilders"
version = "1.0"

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    enabled = true
}