plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"

    kotlin("jvm") version "1.9.0"

    application
}

rootProject.group = "com.ryderbelserion"
rootProject.description = "A discord bot."
rootProject.version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains", "annotations", "24.0.1")

    implementation("com.google.code.gson", "gson", "2.10.1")

    implementation("net.dv8tion", "JDA", "5.0.0-beta.12")
    implementation("ch.qos.logback", "logback-classic", "1.4.5")

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.4")
}

kotlin {
    jvmToolchain(17)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            javaParameters = true
        }
    }

    shadowJar {
        archiveBaseName.set("${rootProject.name}+${rootProject.version}")
    }

    application {
        mainClass.set("com.ryderbelserion.Main")
    }
}