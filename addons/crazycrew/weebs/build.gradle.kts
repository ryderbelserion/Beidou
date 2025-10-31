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
    implementation("com.ryderbelserion.fusion:fusion-addons:0.3.0")

    compileOnly("ch.qos.logback:logback-classic:1.5.20")
    compileOnly("net.sf.jopt-simple:jopt-simple:5.0.4")
    compileOnly("net.dv8tion:JDA:6.1.0")

    compileOnly(project(":beidou-bot"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(24)
    }
}