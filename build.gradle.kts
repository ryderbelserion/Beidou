plugins {
    id("com.gradleup.shadow") version "9.2.2"

    `java-library`

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
    implementation("com.ryderbelserion.fusion:fusion-addons:0.1.0")

    implementation("ch.qos.logback:logback-classic:1.5.20")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("net.dv8tion:JDA:6.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

application {
    mainClass.set("com.ryderbelserion.discord.Main")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(24)
    }

    shadowJar {
        manifest {
            attributes["Main-Class"] = "com.ryderbelserion.discord.Main"
        }

        archiveClassifier.set("")
    }
}