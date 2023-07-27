package com.ryderbelserion.bot

import com.ryderbelserion.api.DedicatedModule
import com.ryderbelserion.bot.commands.AboutCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class Krul : DedicatedModule(
    listOf(
        GatewayIntent.MESSAGE_CONTENT,
        GatewayIntent.SCHEDULED_EVENTS,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_PRESENCES,
        GatewayIntent.GUILD_VOICE_STATES,
        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
    ),
    listOf(
        CacheFlag.VOICE_STATE,
        CacheFlag.EMOJI
    )
) {

    override fun onStart() {
        println("Guten Tag!")
    }

    override fun onReady(jda: JDA) {
        listeners {
            register(AboutCommand())
        }

        println("${jda.selfUser.name} is ready!")
    }

    override fun onGuildReady(guild: Guild) {
        createGuildDir(guild.idLong, "guilds")

        commands(guild) {
            addGuildCommand(AboutCommand())
        }

        println("${guild.name} is ready!")
    }

    override fun onStop(jda: JDA) {
        println("${jda.selfUser.name} is offline!")
    }

    override fun token(): String? {
        return System.getenv("discord_token")
    }
}