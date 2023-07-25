package com.ryderbelserion.bot

import com.ryderbelserion.api.DedicatedModule
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File

class Krul : DedicatedModule(
    listOf(
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.MESSAGE_CONTENT,
        GatewayIntent.GUILD_PRESENCES,
        GatewayIntent.SCHEDULED_EVENTS,
        GatewayIntent.GUILD_VOICE_STATES,
        GatewayIntent.GUILD_EMOJIS_AND_STICKERS
    ),
    listOf(
        CacheFlag.VOICE_STATE
    )
) {

    override fun onStart() {
        println("Guten Tag!")
    }

    override fun onReady() {
        println("Bot is ready!")
    }

    override fun onGuildReady(guild: Guild) {
        val folder = dataFolder.resolve("guilds")

        if (!folder.exists()) folder.mkdir()
        if (folder.exists() && !folder.resolve(guild.id).exists()) folder.resolve(guild.id).mkdir()

        println("${guild.name} is ready!")
    }

    override fun onStop() {
        println("Bot is offline!")
    }

    override fun token(): String? {
        return System.getenv("discord_token")
    }
}