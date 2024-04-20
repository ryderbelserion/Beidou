package com.ryderbelserion.bot.commands.minecraft

import com.ryderbelserion.api.commands.CommandContext
import com.ryderbelserion.api.commands.CommandEngine
import com.ryderbelserion.api.embeds.Embed
import com.ryderbelserion.api.embeds.EmbedColors
import com.ryderbelserion.bot.Beidou
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.Permission

public class MinecraftCommand(private val plugin: Beidou) : CommandEngine("minecraft", "A set of commands related to Minecraft", Permission.MESSAGE_SEND) {

    override fun perform(context: CommandContext) {
        val ip = context.getOption("server")?.asString

        if (ip.isNullOrEmpty() || ip.isBlank()) {
            context.reply("The server IP is empty or null", true)

            return
        }

        runCatching {
            val icon: String
            val server: Server

            runBlocking {
                val ms = MinecraftServer(ip)

                icon = ms.getIcon()

                server = ms.getServer()
            }

            val color = if (server.isOnline) EmbedColors.SUCCESS else EmbedColors.FAIL

            val embed = Embed()
                .author(context.author())
                .fields {
                    field("Server IP", ip, true)
                }
                .thumbnail(icon)
                .color(color)

            if (server.isOnline) {
                embed.fields {
                    field("Server Version", server.protocol.version, true)
                    field("Online/Max", "${server.players.playerCount}/${server.players.maxCount}", true)
                }
            }

            context.reply(embed.build(), false)
        }.onFailure {
            val embed = Embed()
                .author(context.author())
                .description("$ip is not online. Retrieved cached data!")
                .fields {
                    field("Server Version", "1.20.4", true)
                    field("Online/Max", "0/100", true)
                }

                .color(EmbedColors.FAIL)

            context.reply(embed.build(), false)
        }
    }
}

public class MinecraftServer(private val ip: String) {
    private val url = "https://api.mcsrvstat.us/3/$ip"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    public fun getIcon(): String {
        return "https://api.mcsrvstat.us/icon/$ip"
    }

    public suspend fun getServer(): Server {
        return this.client.get(this.url).body<Server>()
    }
}

@Serializable
public data class Server(@SerialName("online") val isOnline: Boolean, val players: Players, val protocol: Protocol)

@Serializable
public data class Players(@SerialName("online") val playerCount: String, @SerialName("max") val maxCount: String)

@Serializable
public data class Protocol(@SerialName("name") val version: String)