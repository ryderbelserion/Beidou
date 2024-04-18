package com.ryderbelserion.api

import com.ryderbelserion.api.commands.CommandHandler
import com.ryderbelserion.api.listeners.ListenerBuilder
import com.ryderbelserion.api.listeners.ModuleListener
import com.ryderbelserion.api.util.exceptions.ModuleInitializeException
import com.ryderbelserion.api.util.scheduler.Scheduler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

public abstract class DedicatedModule(
    private val gateways: List<GatewayIntent> = emptyList(),
    private val cache: List<CacheFlag> = emptyList(),
    name: String,
    private val extra: DedicatedModule.() -> Unit = {}
) : VitalDiscord(name) {

    private var isActive: Boolean = false

    private val jda: JDA = jda()

    private fun jda(): JDA {
        return JDABuilder.createDefault(token(), this.gateways)
            .enableCache(this.cache)
            .addEventListeners(ModuleListener(this, this.extra))
            .build()
    }

    public fun DedicatedModule.listeners(configuration: ListenerBuilder.() -> Unit): ListenerBuilder {
        return ListenerBuilder(this.jda).apply(configuration)
    }

    public fun DedicatedModule.commands(guild: Guild, configuration: CommandHandler.() -> Unit): CommandHandler {
        return CommandHandler().setJDA(this.jda).setGuild(guild).apply(configuration)
    }

    public fun DedicatedModule.commands(configuration: CommandHandler.() -> Unit): CommandHandler {
        return CommandHandler().setJDA(this.jda).apply(configuration)
    }

    public override fun init(): Boolean {
        runCatching {
            if (!getDirectory().exists()) getDirectory().mkdir()

            if (!getAddonDirectory().exists()) getAddonDirectory().mkdir()

            if (!getCacheDirectory().exists()) getCacheDirectory().mkdir()

            if (!getGuildDirectory().exists()) getGuildDirectory().mkdir()

            Scheduler.start()

            onStart()
        }.onFailure {
            this.isActive = false

            throw ModuleInitializeException("Could not enable the dedicated module!")
        }.onSuccess {
            this.isActive = true
        }

        return this.isActive
    }

    public override fun isActive(): Boolean {
        return this.isActive
    }

    public fun createGuildDir(id: Long) {
        val folder = getGuildDirectory(id.toString())

        if (!folder.exists()) folder.mkdir()
    }
}