plugins {
    id("root-plugin")
}

dependencies {
    api("com.zaxxer", "HikariCP", "5.1.0")

    api("com.google.code.gson", "gson", "2.10.1")

    api("net.dv8tion", "JDA", "5.0.0-beta.18")
    api("ch.qos.logback", "logback-classic", "1.4.11")

    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.7.3")
}