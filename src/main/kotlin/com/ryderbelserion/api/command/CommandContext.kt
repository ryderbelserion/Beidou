package com.ryderbelserion.api.command

import com.ryderbelserion.api.command.builders.CommandActor
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CommandContext(private val event: SlashCommandInteractionEvent): CommandActor() {

    override fun reply(ephemeral: Boolean, message: String) {
        event.reply(message).setEphemeral(ephemeral).queue()
    }

    override fun reply(embed: MessageEmbed, ephemeral: Boolean) {
        event.replyEmbeds(embed).setEphemeral(ephemeral).queue()
    }

    override fun author(): User? {
        return event.member?.user
    }

    override fun creator(): User? {
        return jda().getUserById("209853986646261762")
    }

    override fun bot(): SelfUser {
        return jda().selfUser
    }

    override fun guild(): Guild? {
        return event.guild
    }

    override fun jda(): JDA {
        return event.jda
    }
}