package com.ryderbelserion.api.builders.embeds

import com.ryderbelserion.api.util.ColorUtil.toColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.time.LocalDateTime
import java.time.ZoneId

class Embed {

    private val builder = EmbedBuilder()
    private val fields = Fields(this.builder)

    /**
     * Sets the title of the embed.
     *
     * @param text the text in the title.
     * @return the embed class with updated information.
     */
    fun title(text: String): Embed {
        this.builder.setTitle(text)

        return this
    }

    /**
     * Sets the footer using text/icon
     *
     * @param text the text in the footer.
     * @param icon the icon in the footer.
     * @return the embed class with updated information.
     */
    fun footer(text: String, icon: String? = null): Embed {
        this.builder.setFooter(text, icon)

        return this
    }

    /**
     * Sets the footer using the user object.
     *
     * @param user the user in the footer.
     * @return the embed class with updated information.
     */
    fun footer(user: User): Embed {
        this.builder.setFooter("Requested by: ${user.asMention}", user.effectiveAvatarUrl)

        return this
    }

    /**
     * Set the footer using the user object.
     *
     * @param user - The member in question.
     */
    fun footer(user: User, guild: Guild): Embed {
        val avatar = guild.getMember(user)?.effectiveAvatarUrl

        this.builder.setFooter("Requested by: ${user.asMention}", avatar)

        return this
    }

    /**
     * Sets the description of the embed.
     *
     * @param text the text to use.
     * @return the embed class with updated information.
     */
    fun description(text: String): Embed {
        this.builder.setDescription(text)

        return this
    }

    /**
     * Sets the thumbnail using a url.
     *
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    fun thumbnail(url: String): Embed {
        this.builder.setThumbnail(url)

        return this
    }

    /**
     * Set the thumbnail using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun thumbnail(user: User, guild: Guild?): Embed {
        val avatar = user.let { guild?.getMember(it)?.effectiveAvatarUrl }

        this.builder.setThumbnail(avatar)

        return this
    }

    /**
     * Sets the thumbnail using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    fun thumbnail(user: User): Embed {
        this.builder.setThumbnail(user.effectiveAvatarUrl)

        return this
    }

    /**
     * Sets the embed image using a url.
     *
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    fun image(url: String): Embed {
        this.builder.setImage(url)

        return this
    }

    /**
     * Sets the embed image using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    fun image(user: User): Embed {
        this.builder.setImage(user.effectiveAvatarUrl)

        return this
    }

    /**
     * Sets the author using name/url
     *
     * @param name the name to use.
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    fun author(name: String, url: String? = null): Embed {
        this.builder.setAuthor(name, null, url)

        return this
    }

    /**
     * Sets the author using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    fun author(user: User): Embed {
        this.builder.setAuthor(user.effectiveName, null, user.effectiveAvatarUrl)

        return this
    }

    /**
     * Set the author using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun author(user: User, guild: Guild?): Embed {
        val member = user.let { guild?.getMember(it) }

        val avatar = member?.effectiveAvatarUrl

        this.builder.setAuthor(member?.effectiveName, null, avatar)

        return this
    }

    /**
     * Sets the color of the embed.
     *
     * @param value the color to use.
     * @return the embed class with updated information.
     */
    fun color(value: String): Embed {
        this.builder.setColor(value.toColor())

        return this
    }

    /**
     * Set a color using one of our pre-set colors.
     *
     * @param color - A preset enum of colors.
     */
    fun color(color: EmbedColors): Embed {
        this.builder.setColor(color.code.toColor())

        return this
    }

    /**
     * Sets the timezone in the embed.
     *
     * @param timezone the timezone to use for embeds.
     * @return the embed class with updated information.
     */
    fun timestamp(timezone: String = "America/New_York"): Embed {
        this.builder.setTimestamp(LocalDateTime.now().atZone(ZoneId.of(timezone)))

        return this
    }

    /**
     * Add multiple fields to the embed.
     *
     * @param block the list of fields to add.
     * @return the embed class with updated information.
     */
    fun fields(block: Fields.() -> Unit): Embed {
        block(this.fields)

        return this
    }

    /**
     * @return the built embed.
     */
    fun build(): MessageEmbed {
        return this.builder.build()
    }
}

/**
 * @return the built embed.
 */
 inline fun embed(builder: Embed.() -> Unit): MessageEmbed {
    return Embed().apply(builder).build()
}