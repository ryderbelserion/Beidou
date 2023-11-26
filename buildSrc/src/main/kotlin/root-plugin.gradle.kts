plugins {
    kotlin("jvm")
}

dependencies {
    api(kotlin("stdlib"))
}

repositories {
    mavenCentral()
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
}