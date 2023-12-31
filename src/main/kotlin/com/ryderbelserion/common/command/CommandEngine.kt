package com.ryderbelserion.common.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class CommandEngine(val name: String, val desc: String, val permission: Permission) : ListenerAdapter() {

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