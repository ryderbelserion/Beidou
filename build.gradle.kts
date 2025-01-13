import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.gradleup.shadow") version "9.0.0-beta4"

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
    implementation("net.dv8tion", "JDA", "5.2.2")

    implementation("ch.qos.logback", "logback-classic", "1.5.5")

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