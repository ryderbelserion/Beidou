package com.ryderbelserion.api.builders.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class CommandEngine(
    val name: String,
    val description: String,
    val permission: Permission
) : ListenerAdapter() {

    protected abstract fun perform(context: CommandContext)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val context = CommandContext(event)

        if (!event.name.equals(this.name, ignoreCase = true)) return

        perform(context)
    }
}