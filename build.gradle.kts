plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"

    kotlin("jvm") version "1.9.21"

    application
}

base {
    archivesName.set(rootProject.name)
}

rootProject.group = "com.ryderbelserion"
rootProject.description = "A discord bot."
rootProject.version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.7.3")

    implementation("ch.qos.logback", "logback-classic", "1.4.14")

    implementation("com.squareup.moshi", "moshi", "1.15.0")

    implementation("net.dv8tion", "JDA", "5.0.0-beta.20")

    implementation(kotlin("stdlib"))
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