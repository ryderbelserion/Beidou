plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"

    kotlin("jvm") version "1.9.23"

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
    api("com.ryderbelserion.vital", "discord", "1.5")

    implementation("com.squareup.moshi", "moshi", "1.15.0")
}

kotlin {
    jvmToolchain(17)
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