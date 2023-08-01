package com.ryderbelserion.api.command

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command

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

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        if (event.name != name) return

        val words = arrayOf("apple", "apricot", "banana", "cherry", "coconut", "cranberry")

        val options = words.filter { word ->
            word.startsWith(event.focusedOption.value)
        }.map { word ->
            Command.Choice(word, word)
        }

        event.replyChoice(options.toString(), "").queue()
    }
}