package com.ryderbelserion.api.plugin

import com.ryderbelserion.api.plugin.registry.ModuleRegistration
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

abstract class ModulePlugin {

    abstract fun onReady(jda: JDA)

    abstract fun onStart()

    abstract fun onGuildReady(guild: Guild)

    abstract fun onStop(jda: JDA)

    abstract fun token(): String?

    abstract fun init(): Boolean

    fun enable() {
        ModuleRegistration.start(this)
    }

    fun disable() {
        ModuleRegistration.stop()
    }
}