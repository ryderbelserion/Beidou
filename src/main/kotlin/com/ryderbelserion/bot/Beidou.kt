package com.ryderbelserion.bot

import com.ryderbelserion.bot.commands.AboutCommand
import com.ryderbelserion.api.DedicatedModule
import com.ryderbelserion.bot.commands.minecraft.MinecraftCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

public class Beidou : DedicatedModule(
    listOf(
        GatewayIntent.DIRECT_MESSAGES,
        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
        GatewayIntent.DIRECT_MESSAGE_TYPING,
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

    override fun onGuildReady(guild: Guild) {
        createGuildDir(guild.idLong)
    }

    override fun onReady(jda: JDA) {
        commands {
            addCommand(AboutCommand())

            addCommand(
                MinecraftCommand(this@Beidou), OptionData(
                OptionType.STRING,
                "server",
                "The ip of the minecraft server"
            ))
        }

        listeners {
            register(
                MinecraftCommand(this@Beidou),
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