package com.ryderbelserion.common

import com.ryderbelserion.common.command.CommandManager
import com.ryderbelserion.common.utils.exceptions.ModuleInitializeException
import com.ryderbelserion.common.utils.listeners.ListenerBuilder
import com.ryderbelserion.common.utils.listeners.ModuleListener
import com.ryderbelserion.common.utils.scheduler.Scheduler
import com.ryderbelserion.common.files.FileManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File

abstract class DedicatedModule(
    private val gateways: List<GatewayIntent> = emptyList(),
    private val cache: List<CacheFlag> = emptyList(),
    private val file: File,
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

    open fun getDataFolder() = this.file

    open fun getGuildFolder(path: String) = getDataFolder().resolve(path)

    open fun getGuildFolderID(path: String, id: Long) = getGuildFolder(path).resolve(id.toString())

    open fun getAddonFolder() = getDataFolder().resolve("addons")
}