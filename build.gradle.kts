plugins {
    application
}

base {
    archivesName.set(rootProject.name)
}

rootProject.group = "com.ryderbelserion"
rootProject.description = "A discord bot."
rootProject.version = "1.0"

dependencies {
    implementation(project(":common"))
}

tasks {
    application {
        mainClass.set("com.ryderbelserion.Main")
    }
}