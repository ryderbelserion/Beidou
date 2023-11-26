package com.ryderbelserion.common.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color
import java.time.LocalDateTime
import java.time.ZoneId

class Embed {

    private val embed = EmbedBuilder()
    private val fields = Fields(embed)

    init {
        embed.setColor(EmbedColors.DEFAULT.code.toColor())
    }

    /**
     * Set the default title.
     */
    fun title(title: String): Embed {
        embed.setTitle(title)

        return this
    }

    /**
     * Set the footer using strings.
     *
     * @param footer - The text to put in the footer.
     * @param icon - The icon whether it's a profile picture or something random.
     */
    fun footer(footer: String, icon: String? = null): Embed {
        embed.setFooter(footer, icon)

        return this
    }

    fun timestamp(): Embed {
        embed.setTimestamp(LocalDateTime.now().atZone(ZoneId.of("America/New_York")))

        return this
    }

    /**
     * Set the footer using the user object.
     *
     * @param user - The member in question.
     */
    fun footer(user: User, guild: Guild?): Embed {
        val avatar = guild?.getMember(user)?.effectiveAvatarUrl

        embed.setFooter("Requested by: ${user.asMention}", avatar)

        return this
    }

    /**
     * Add a list of fields.
     */
    fun fields(block: Fields.() -> Unit): Embed {
        block(fields)

        return this
    }

    /**
     * Set the hex color to a color of our choice.
     *
     * @param hex - The color to choose.
     */
    fun color(hex: String): Embed {
        embed.setColor(hex.toColor())

        return this
    }

    /**
     * Set a color using one of our pre-set colors.
     *
     * @param color - A preset enum of colors.
     */
    fun color(color: EmbedColors): Embed {
        embed.setColor(color.code.toColor())

        return this
    }

    /**
     * Set thumbnails using strings.
     *
     * @param url - The image url to use.
     */
    fun thumbnail(url: String): Embed {
        embed.setThumbnail(url)

        return this
    }

    /**
     * Set the thumbnail using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun thumbnail(user: User?, guild: Guild?): Embed {
        val avatar = user?.let { guild?.getMember(it)?.effectiveAvatarUrl }

        embed.setThumbnail(avatar)

        return this
    }

    /**
     * Set the image using a string.
     *
     * @param url - The image url to use.
     */
    fun image(url: String?): Embed {
        embed.setImage(url)

        return this
    }

    /**
     * Set the author using strings.
     *
     * @param name - The name of the author.
     * @param image - An optional value to input an image url.
     */
    fun author(name: String?, image: String? = null): Embed {
        embed.setAuthor(name, null, image)

        return this
    }

    /**
     * Set the author using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun author(user: User?, guild: Guild?): Embed {
        val member = user?.let { guild?.getMember(it) }

        val avatar = member?.effectiveAvatarUrl

        embed.setAuthor(member?.effectiveName, null, avatar)

        return this
    }

    /**
     * Set the description of the embed.
     *
     * @param description - The description to use.
     */
    fun description(description: String): Embed {
        embed.setDescription(description)

        return this
    }

    fun build(): MessageEmbed {
        return embed.build()
    }
}

/**
 * Build the embed.
 */
inline fun embed(builder: Embed.() -> Unit): MessageEmbed {
    return Embed().apply(builder).build()
}

fun String.toColor(): Color {
    return Color(
        Integer.valueOf(substring(1, 3), 16),
        Integer.valueOf(substring(3, 5), 16),
        Integer.valueOf(substring(5, 7), 16)
    )
}