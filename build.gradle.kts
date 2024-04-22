plugins {
    kotlin("jvm") version libs.versions.core.kotlin.get()
    id("io.ktor.plugin") version libs.versions.core.ktor.get()
}

group = "com.ougi.callme"
version = "0.0.1"

application {
    mainClass.set("com.ougi.callme.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.core.jvm)
    implementation(libs.ktor.openapi)
    implementation(libs.ktor.auth.jvm)
    implementation(libs.ktor.auth.jwt.jvm)
    implementation(libs.ktor.netty.jvm)
    implementation(libs.netty.logback)
}
