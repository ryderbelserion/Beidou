import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.gradleup.shadow") version "9.2.2"

    kotlin("jvm") version "2.1.0"

    application
}


rootProject.group = "com.ryderbelserion.beidou"
rootProject.description = "a discord bot"
rootProject.version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion", "JDA", "5.6.1")

    implementation("ch.qos.logback", "logback-classic", "1.5.18")

    implementation(kotlin("stdlib"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        javaParameters = true
    }

    jvmToolchain(21)

    explicitApi()
}

application {
    mainClass.set("com.ryderbelserion.MainKt")
}