package com.ryderbelserion.api.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class CommandEngine(val name: String, val desc: String) : ListenerAdapter() {

    abstract fun perform(context: CommandContext)

    open fun execute(context: CommandContext) {
        perform(context)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val context = CommandContext(event)

        if (event.name != name) return

        execute(context)
    }
}