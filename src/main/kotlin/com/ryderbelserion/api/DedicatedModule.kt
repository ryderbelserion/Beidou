package com.ryderbelserion.api

import com.ryderbelserion.api.command.CommandManager
import com.ryderbelserion.api.utils.exceptions.ModuleInitializeException
import com.ryderbelserion.api.utils.listeners.ListenerBuilder
import com.ryderbelserion.api.utils.listeners.ModuleListener
import com.ryderbelserion.api.utils.scheduler.Scheduler
import com.ryderbelserion.api.files.FileManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File

abstract class DedicatedModule(
    private val gateways: List<GatewayIntent> = emptyList(),
    private val cache: List<CacheFlag> = emptyList(),
    private val extra: DedicatedModule.() -> Unit = {}
) : ModulePlugin() {

    private var isActive: Boolean = false

    private val jda: JDA = get()

    val handler = FileManager()

    private fun get(): JDA {
        return JDABuilder.createDefault(token(), gateways).enableCache(cache).addEventListeners(getListener()).build()
    }

    private fun getListener(): ModuleListener {
        return ModuleListener(this, extra)
    }

    fun DedicatedModule.listeners(configuration: ListenerBuilder.() -> Unit): ListenerBuilder {
        return ListenerBuilder(jda).apply(configuration)
    }

    fun DedicatedModule.commands(guild: Guild, configuration: CommandManager.() -> Unit): CommandManager {
        return CommandManager().setJDA(jda).setGuild(guild).apply(configuration)
    }

    fun DedicatedModule.commands(configuration: CommandManager.() -> Unit): CommandManager {
        return CommandManager().setJDA(jda).apply(configuration)
    }

    override fun init(): Boolean {
        runCatching {
            if (!getDataFolder().exists()) getDataFolder().mkdir()

            if (!getAddonFolder().exists() && getAddonFolder().exists()) getAddonFolder().mkdir()

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

    fun isActive(): Boolean {
        return this.isActive
    }

    fun createGuildDir(id: Long, path: String) {
        val folder = getDataFolder().resolve(path)

        if (!folder.exists()) folder.mkdir()
        if (folder.exists() && !folder.resolve(id.toString()).exists()) folder.resolve(id.toString()).mkdir()
    }

    open fun getDataFolder() = File("./bot")

    open fun getGuildFolder(path: String) = getDataFolder().resolve(path)

    open fun getGuildFolderID(path: String, id: Long) = getGuildFolder(path).resolve(id.toString())

    open fun getAddonFolder() = getDataFolder().resolve("addons")
}