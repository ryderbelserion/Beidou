package com.ryderbelserion.api.builders.commands.interfaces

import com.ryderbelserion.api.builders.commands.CommandContext
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionMapping

interface CommandActor {

    /**
     * Sends a simple string with an ephemeral toggle.
     */
    fun reply(message: String, ephemeral: Boolean)

    /**
     * Sends an embed with an ephemeral toggle.
     */
    fun reply(embed: MessageEmbed, ephemeral: Boolean)

    /**
     * Defer a reply, Sends the thinking... message to a user.
     *
     * @return the object
     */
    fun defer(ephemeral: Boolean): CommandContext?

    /**
     * Gets the option from the event.
     */
    fun getOption(option: String): OptionMapping?

    /**
     * @author of the message.
     */
    fun author(): User?

    /**
     * @return the one who created the bot.
     */
    fun creator(): User?

    /**
     * Checks if the user is the creator of the bot.
     * @param id the id
     * @return true or false
     */
    fun isCreator(id: String?): Boolean

    /**
     * @return the bot.
     */
    fun bot(): SelfUser?

    /**
     * @return guild the command is executed in.
     */
    fun guild(): Guild?

    /**
     * @return jda instance.
     */
    fun jda(): JDA?

}