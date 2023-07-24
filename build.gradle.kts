plugins {
    `java-library`
}

rootProject.group = "com.ryderbelserion.beidou"
rootProject.description = "A discord bot."
rootProject.version = "1.0.0-rc1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of("17"))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
}