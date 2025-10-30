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
    private val fields = EmbedField(this.builder)

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
        return footer("Requested by: ${user.asMention}", user.getEffectiveAvatarUrl())
    }

    /**
     * Set the footer using the user object.
     *
     * @param user - The member in question.
     */
    fun footer(user: User, guild: Guild): Embed {
        val member = guild.getMemberById(user.id)

        return footer(
            "Requested by: ${user.asMention}",
            member?.getEffectiveAvatarUrl() ?: user.getEffectiveAvatarUrl()
        )
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
     * Set the thumbnail using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun thumbnail(user: User, guild: Guild): Embed {
        val member = guild.getMemberById(user.getId())

        return thumbnail(member?.getEffectiveAvatarUrl() ?: user.getEffectiveAvatarUrl())
    }

    /**
     * Sets the thumbnail using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    fun thumbnail(user: User): Embed {
        return thumbnail(user.getEffectiveAvatarUrl())
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
     * Set the image using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun image(user: User, guild: Guild): Embed {
        val member = guild.getMemberById(user.id)

        return image(member?.getEffectiveAvatarUrl() ?: user.getEffectiveAvatarUrl())
    }

    /**
     * Sets the embed image using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    fun image(user: User): Embed {
        this.builder.setImage(user.getEffectiveAvatarUrl())

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
     * Sets the author using name/url
     *
     * @param name the name to use.
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    fun author(name: String, url: String?): Embed {
        this.builder.setAuthor(name, null, url)

        return this
    }

    /**
     * Set the author using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    fun author(user: User, guild: Guild): Embed {
        val member = guild.getMemberById(user.getId())

        val avatar = if (member == null) user.getEffectiveAvatarUrl() else member.getEffectiveAvatarUrl()

        return author(user.getEffectiveName(), avatar)
    }

    /**
     * Sets the author using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    fun author(user: User): Embed {
        return author(user.getEffectiveName(), user.getEffectiveAvatarUrl())
    }

    /**
     * Sets the color of the embed.
     *
     * @param color the color to use.
     * @return the embed class with updated information.
     */
    fun color(color: String): Embed {
        this.builder.setColor(color.toColor())

        return this
    }

    /**
     * Set a color using one of our pre-set colors.
     *
     * @param color - A preset enum of colors.
     */
    fun color(color: EmbedColor): Embed {
        this.builder.setColor(color.color)

        return this
    }

    /**
     * Sets the timezone in the embed.
     *
     * @param timezone the timezone to use for embeds.
     * @return the embed class with updated information.
     */
    fun timestamp(timezone: String): Embed {
        this.builder.setTimestamp(LocalDateTime.now().atZone(ZoneId.of(timezone)))

        return this
    }

    /**
     * Sets the timezone in the embed.
     *
     * @return the embed class with updated information.
     */
    fun timestamp(): Embed {
        return timestamp("America/New_York")
    }

    /**
     * Add multiple fields to the embed.
     *
     * @param fields the list of fields to add.
     * @return the embed class with updated information.
     */
    fun fields(fields: EmbedField.() -> Unit): Embed {
        fields(this.fields)

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