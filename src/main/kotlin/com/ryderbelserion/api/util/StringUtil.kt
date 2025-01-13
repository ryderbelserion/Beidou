package com.ryderbelserion.api.util

import com.ryderbelserion.api.embeds.Embed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

public object StringUtil {

    /**
     * Send a message
     */
    public fun sendMessage(message: String, id: Long, guild: Guild) {
        if (message.isEmpty() || message.isBlank()) return

        val channel = guild.getTextChannelById(id) ?: return

        channel.sendMessage(message).queue()
    }

    /**
     * Send a message
     */
    public fun sendMessage(
        author: Member,
        title: String,
        description: String?,
        color: String,
        id: Long,
        guild: Guild
    ) {
        val embed = Embed()

        embed.author(title, author.avatarUrl)

        if (description != null) {
            embed.description(description)
        }

        color(color, embed, id, guild)
    }

    @OptIn(ExperimentalEncodingApi::class)
    public fun convertToImage(base64: String, directory: File, overwrite: Boolean = false): File {
        val data: ByteArray = Base64.decode(base64, 0)

        if (!Files.exists(Path.of("${directory.path}/icon.png")) || overwrite) {
            FileOutputStream("${directory.path}/icon.png").use { stream ->
                stream.write(data)
            }
        }

        return directory.resolve("icon.png")
    }

    /**
     * Send a message
     */
    public fun sendMessage(description: String, color: String, id: Long, guild: Guild) {
        val embed = Embed()

        embed.description(description)

        color(color, embed, id, guild)
    }

    /**
     * Send a message
     */
    private fun color(color: String, embed: Embed, id: Long, guild: Guild) {
        embed.color(color)

        val messageEmbed = embed.build()
        val channel = guild.getTextChannelById(id) ?: return

        channel.sendMessageEmbeds(messageEmbed).queue()
    }
}