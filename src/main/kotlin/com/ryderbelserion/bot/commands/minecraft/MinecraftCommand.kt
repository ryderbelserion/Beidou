package com.ryderbelserion.bot.commands.minecraft

import com.ryderbelserion.api.commands.CommandContext
import com.ryderbelserion.api.commands.CommandEngine
import com.ryderbelserion.api.embeds.Embed
import com.ryderbelserion.api.embeds.EmbedColors
import com.ryderbelserion.api.util.StringUtil
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
import java.nio.file.Files
import java.nio.file.Path

public class MinecraftCommand(private val plugin: Beidou) : CommandEngine("minecraft", "A set of commands related to Minecraft", Permission.MESSAGE_SEND) {

    override fun perform(context: CommandContext) {
        val ip = context.getOption("server")?.asString

        if (ip.isNullOrEmpty() || ip.isBlank()) {
            context.reply("The server IP is empty or null", true)

            return
        }

        var server: Server

        runBlocking {
            server = MinecraftServer(ip).getServer()
        }

        val color = if (server.isOnline) EmbedColors.SUCCESS else EmbedColors.FAIL

        val image = StringUtil.convertToImage(server.icon.split(",")[1], this.plugin.getGuildDirectory(context.guild()?.id)).path

        val embed = Embed()
            .author(context.author())
            .fields {
                field("Server IP", ip, true)
                field("Online/Max", "${server.players.playerCount}/${server.players.maxCount}", true)
            }
            .thumbnail("attachment://$image")
            .color(color)

        context.reply(embed.build(), false)
    }
}

public class MinecraftServer(ip: String) {
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

    public suspend fun getServer(): Server {
        val response: Server = this.client.get(this.url).body()

        return response
    }
}

@Serializable
public data class Server(@SerialName("online") val isOnline: Boolean, val players: Players, val icon: String)

@Serializable
public data class Players(@SerialName("online") val playerCount: String, @SerialName("max") val maxCount: String)