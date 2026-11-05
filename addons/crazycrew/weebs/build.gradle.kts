plugins {
    `java-library`
}

project.group = "com.badbones69.weebs"
project.version = "1.0.0"

repositories {
    maven("https://repo.crazycrew.us/snapshots/")
    maven("https://repo.crazycrew.us/releases/")

    mavenCentral()
}

dependencies {
    compileOnly(project(":beidou-bot"))

    compileOnly(libs.bundles.discord)
    compileOnly(libs.bundles.fusion)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(25)
    }
}