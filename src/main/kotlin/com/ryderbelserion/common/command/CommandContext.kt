package com.ryderbelserion.common.command

import com.ryderbelserion.common.command.interfaces.CommandActor
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CommandContext(private val event: SlashCommandInteractionEvent): CommandActor() {

    /**
     * Sends a simple string with an ephemeral toggle
     */
    override fun reply(message: String, ephemeral: Boolean) {
        this.event.reply(message).setEphemeral(ephemeral).queue()
    }

    /**
     * Sends an embed with an ephemeral toggle
     */
    override fun reply(embed: MessageEmbed, ephemeral: Boolean) {
        this.event.replyEmbeds(embed).setEphemeral(ephemeral).queue()
    }

    /**
     * @author of the message
     */
    override fun author(): User? {
        return this.event.member?.user
    }

    /**
     * @return the one who created the bot
     */
    override fun creator(): User? {
        return jda().getUserById("209853986646261762")
    }

    /**
     * @return the bot
     */
    override fun bot(): SelfUser {
        return jda().selfUser
    }

    /**
     * @return guild the command is executed in
     */
    override fun guild(): Guild? {
        return this.event.guild
    }

    /**
     * @return jda instance
     */
    override fun jda(): JDA {
        return this.event.jda
    }
}