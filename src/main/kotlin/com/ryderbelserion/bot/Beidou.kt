package com.ryderbelserion.bot

import com.ryderbelserion.api.DiscordPlugin
import com.ryderbelserion.bot.commands.AboutCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import java.nio.file.Files

class Beidou(token: String, logger: Logger) : DiscordPlugin(
    username = "beidou",
    token = token,
    logger = logger,
    intents = listOf(
        // direct messages
        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
        GatewayIntent.DIRECT_MESSAGE_TYPING,
        GatewayIntent.DIRECT_MESSAGES,

        // universal
        GatewayIntent.MESSAGE_CONTENT,

        // other
        GatewayIntent.SCHEDULED_EVENTS,

        // guilds
        GatewayIntent.GUILD_VOICE_STATES,
        GatewayIntent.GUILD_EXPRESSIONS,
        GatewayIntent.GUILD_PRESENCES,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_WEBHOOKS
    ),
    flags = listOf(
        CacheFlag.VOICE_STATE,
        CacheFlag.EMOJI
    )
) {
    override fun onGuildReady(guild: Guild) {
        val id = guild.id

        //todo() check if guilds.json contains guild id

        val folder = getGuildDirectory(id)

        if (!Files.exists(folder)) {
            Files.createDirectory(folder)

            this.logger.info("Created guild $folder!")
        }
    }

    override fun onReady(jda: JDA) {
        commands {
            addCommand(AboutCommand())

            /*addCommand(MinecraftCommand(this@Beidou),
                listOf(
                    OptionData(OptionType.STRING, "server", "The ip of the minecraft server"),
                    OptionData(OptionType.BOOLEAN, "bedrock", "If you want to look for bedrock servers")
                )
            )*/
        }

        listeners {
            register(
                AboutCommand()
            )
        }

        this.logger.info("${jda.selfUser.name} is ready!")
    }

    override fun onStop(jda: JDA) {
        this.logger.info("${jda.selfUser.name} is offline!")
    }
}