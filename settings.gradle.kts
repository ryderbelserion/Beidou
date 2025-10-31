pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "Beidou"

includeProject("bot")

listOf(
    "addons/crazycrew/weebs" to "weebs"
).forEach {
    includeProject(it.first, it.second)
}

fun includeProject(name: String) {
    includeProject(name) {
        this.name = "${rootProject.name.lowercase()}-$name"
    }
}

fun includeProject(folder: String, name: String) {
    includeProject(name) {
        this.name = "${rootProject.name.lowercase()}-$name"
        this.projectDir = file(folder)
    }
}

fun includeProject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}