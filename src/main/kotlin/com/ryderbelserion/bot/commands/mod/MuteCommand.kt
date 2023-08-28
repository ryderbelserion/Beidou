package com.ryderbelserion.bot.commands.mod

import com.ryderbelserion.api.command.CommandContext
import com.ryderbelserion.api.command.CommandEngine
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class MuteCommand : CommandEngine("mute", "Mutes a discord member.") {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent): Unit = with(event) {
        val context = CommandContext(event)

        if (event.name != name) return
    }
}