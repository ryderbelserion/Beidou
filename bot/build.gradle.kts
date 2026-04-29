plugins {
    alias(libs.plugins.shadow)

    `java-library`

    application
}

rootProject.group = "com.ryderbelserion.beidou"
rootProject.description = "a discord bot"
rootProject.version = "1.2.0"

repositories {
    maven("https://repo.crazycrew.us/releases/")

    mavenCentral()
}

dependencies {
    api(libs.bundles.discord)
    api(libs.bundles.fusion)
    api(libs.hikari)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

application {
    mainClass.set("com.ryderbelserion.discord.Main")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(25)
    }

    shadowJar {
        manifest {
            attributes["Main-Class"] = "com.ryderbelserion.discord.Main"
        }

        archiveBaseName.set("${rootProject.name}-${rootProject.version}")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
}