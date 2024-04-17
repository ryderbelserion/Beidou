package com.ryderbelserion.bot

import com.ryderbelserion.bot.commands.AboutCommand
import com.ryderbelserion.vital.DedicatedModule
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.util.logging.Logger

class Beidou : DedicatedModule(
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
    ),

    "beidou"
) {

    override fun getLogger(): Logger {
        return Logger.getLogger("Beidou")
    }

    override fun onGuildReady(guild: Guild) {

    }

    override fun onReady(jda: JDA) {
        commands {
            addCommand(
                AboutCommand()
            )
        }

        listeners {
            register(
                AboutCommand()
            )
        }

        println("${jda.selfUser.name} is ready!")
    }

    override fun onStart() {

    }

    override fun onStop(jda: JDA) {
        println("${jda.selfUser.name} is offline!")
    }

    override fun token(): String? {
        return System.getenv("staging_discord_token")
    }
}