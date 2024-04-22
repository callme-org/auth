import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version libs.versions.core.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.core.kotlin.get()
    id("io.ktor.plugin") version libs.versions.core.ktor.get()
}

group = "com.ougi.callme"
version = "0.0.1"

application {
    mainClass.set("com.ougi.callme.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_20)
    }
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_20)
        localImageName.set("callme-auth-image")
        imageTag.set(version.toString())
    }
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
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.java)

    implementation(libs.koin)
}
