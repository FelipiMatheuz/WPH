plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    application
}

group = "org.felipimatheuz.wph"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.jsoup:jsoup:1.21.1")

    implementation("com.squareup.okhttp3:okhttp:5.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    testImplementation(kotlin("test"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}