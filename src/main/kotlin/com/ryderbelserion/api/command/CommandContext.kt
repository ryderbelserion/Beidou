package com.ryderbelserion.api.command

import com.ryderbelserion.api.command.builders.CommandActor
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CommandContext(private val event: SlashCommandInteractionEvent): CommandActor() {

    override fun reply(message: String) {
        event.reply(message).queue()
    }

    fun reply(embed: MessageEmbed) {
        event.replyEmbeds(embed).queue()
    }
}