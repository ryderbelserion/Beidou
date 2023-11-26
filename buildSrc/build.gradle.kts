plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.9.21")

    //implementation("com.github.johnrengelman", "shadow", "8.1.1")
}