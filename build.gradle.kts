import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.gradleup.shadow") version "9.2.2"

    kotlin("jvm") version "2.2.21"

    application
}

rootProject.group = "com.ryderbelserion.beidou"
rootProject.description = "a discord bot"
rootProject.version = "1.0.0"

repositories {
    maven("https://repo.crazycrew.us/snapshots/")
    maven("https://repo.crazycrew.us/releases/")

    mavenCentral()
}

dependencies {
    implementation("com.ryderbelserion.fusion:fusion-files:3.0.0-SNAPSHOT")

    implementation("ch.qos.logback:logback-classic:1.5.20")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("net.dv8tion:JDA:6.1.0")

    //implementation("org.jline", "jline", "4.0.0")

    implementation(kotlin("stdlib"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_24)
        javaParameters = true
    }

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

application {
    mainClass.set("com.ryderbelserion.MainKt")
}