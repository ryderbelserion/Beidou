package com.ryderbelserion.api

import com.ryderbelserion.api.builders.commands.CommandHandler
import com.ryderbelserion.api.builders.ListenerBuilder
import com.ryderbelserion.listeners.StatusListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import java.nio.file.Files
import java.nio.file.Path

abstract class DiscordPlugin(
    private val username: String,
    private val token: String,
    protected val logger: Logger,
    private val intents: List<GatewayIntent> = emptyList(),
    private val flags: List<CacheFlag> = emptyList()
) {

    private var isActive: Boolean = false

    private val jda: JDA = jda()

    private fun jda(): JDA {
        return JDABuilder.createDefault(this.token, this.intents)
            .enableCache(this.flags)
            .addEventListeners(StatusListener(this))
            .build()
    }

    fun DiscordPlugin.listeners(configuration: ListenerBuilder.() -> Unit): ListenerBuilder {
        return ListenerBuilder(this.jda).apply(configuration)
    }

    fun DiscordPlugin.commands(guild: Guild, configuration: CommandHandler.() -> Unit): CommandHandler {
        return CommandHandler().setJDA(this.jda).setGuild(guild).apply(configuration)
    }

    fun DiscordPlugin.commands(configuration: CommandHandler.() -> Unit): CommandHandler {
        return CommandHandler().setJDA(this.jda).apply(configuration)
    }

    abstract fun onGuildReady(guild: Guild)

    abstract fun onReady(jda: JDA)

    abstract fun onStop(jda: JDA)

    open fun init() {
        val directory = getDirectory()

        if (!Files.exists(directory)) {
            Files.createDirectory(directory)
        }

        val guildDirectory = getGuildDirectory()

        if (!Files.exists(guildDirectory)) {
            Files.createDirectory(guildDirectory)
        }

        val addonDirectory = getAddonDirectory()

        if (!Files.exists(addonDirectory)) {
            Files.createDirectory(addonDirectory)
        }

        val cacheDirectory = getCacheDirectory()

        if (!Files.exists(cacheDirectory)) {
            Files.createDirectory(cacheDirectory)
        }

        this.logger.info("All ready to go!")
    }

    fun getGuildDirectory(id: String?): Path {
        return getGuildDirectory().resolve(id.toString())
    }

    fun getGuildDirectory(): Path {
        return getDirectory().resolve("guilds")
    }

    fun getAddonDirectory(): Path {
        return getDirectory().resolve("addons")
    }

    fun getCacheDirectory(): Path {
        return getDirectory().resolve("cache")
    }

    fun getDirectory(): Path {
        return Path.of("./${this.username}")
    }
}