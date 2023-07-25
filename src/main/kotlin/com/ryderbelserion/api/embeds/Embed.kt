package com.ryderbelserion.api.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color

class Embed {

    private val embed = EmbedBuilder()
    private val fields = Fields(embed)

    init {
        embed.setColor(EmbedColors.DEFAULT.code.toColor())
    }

    /**
     * Set the default title.
     */
    fun title(title: String) {
        embed.setTitle(title)
    }

    /**
     * Set the footer using strings.
     *
     * @param footer - The text to put in the footer.
     * @param icon - The icon whether it's a profile picture or something random.
     */
    fun footer(footer: String, icon: String? = null) {
        embed.setFooter(footer, icon)
    }

    /**
     * Set the footer using the user object.
     *
     * @param user - The member in question.
     */
    fun footer(user: User, guild: Guild?) {
        val avatar = guild?.getMember(user)?.effectiveAvatarUrl

        embed.setFooter("Requested by: ${user.asMention}", avatar)
    }

    /**
     * Add a list of fields.
     */
    fun fields(block: Fields.() -> Unit) {
        block(fields)
    }

    /**
     * Set the hex color to a color of our choice.
     *
     * @param hex - The color to choose.
     */
    fun color(hex: String) {
        embed.setColor(hex.toColor())
    }

    /**
     * Set a color using one of our pre-set colors.
     *
     * @param color - A preset enum of colors.
     */
    fun color(color: EmbedColors) {
        embed.setColor(color.code.toColor())
    }

    /**
     * Set thumbnails using strings.
     *
     * @param url - The image url to use.
     */
    fun thumbnail(url: String) {
        embed.setThumbnail(url)
    }

    /**
     * Set the thumbnail using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun thumbnail(user: User, guild: Guild?) {
        val avatar = guild?.getMember(user)?.effectiveAvatarUrl

        embed.setThumbnail(avatar)
    }

    /**
     * Set the image using a string.
     *
     * @param url - The image url to use.
     */
    fun image(url: String) {
        embed.setImage(url)
    }

    /**
     * Set the author using strings.
     *
     * @param name - The name of the author.
     * @param image - An optional value to input an image url.
     */
    fun author(name: String?, image: String? = null) {
        embed.setAuthor(name, null, image)
    }

    /**
     * Set the author using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun author(user: User, guild: Guild?) {
        val avatar = guild?.getMember(user)?.effectiveAvatarUrl

        embed.setAuthor(user.asMention, null, avatar)
    }

    /**
     * Set the description of the embed.
     *
     * @param description - The description to use.
     */
    fun description(description: String) {
        embed.setDescription(description)
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