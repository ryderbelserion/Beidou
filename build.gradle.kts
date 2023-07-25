@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version "1.9.0"

    application

    alias(libs.plugins.shadow)
}

rootProject.group = "com.ryderbelserion"
rootProject.description = "A discord bot."
rootProject.version = "1.0.0-rc1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.jetbrains", "annotations", "24.0.1")

    implementation("net.dv8tion", "JDA", "5.0.0-beta.12")
    implementation("ch.qos.logback", "logback-classic", "1.4.5")
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