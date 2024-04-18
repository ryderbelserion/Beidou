plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("io.ktor.plugin") version "2.3.10"

    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"

    application
}

base {
    archivesName.set(rootProject.name)
}

rootProject.group = "com.ryderbelserion"
rootProject.description = "A discord bot."
rootProject.version = "1.1"

repositories {
    maven("https://repo.crazycrew.us/releases/")

    mavenCentral()
}

dependencies {
    implementation("io.ktor", "ktor-client-content-negotiation")
    implementation("io.ktor", "ktor-serialization-kotlinx-json")
    implementation("io.ktor", "ktor-client-core")
    implementation("io.ktor", "ktor-client-cio")

    api(libs.logback.classic)

    api(libs.coroutines.core)

    api(libs.discord.jda)

    api(kotlin("stdlib"))

    implementation("com.squareup.moshi", "moshi", "1.15.0")
}

kotlin {
    jvmToolchain(17)

    explicitApi()
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            javaParameters = true
        }
    }

    application {
        mainClass.set("com.ryderbelserion.Main")
    }
}