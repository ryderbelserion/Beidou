package com.ryderbelserion.bot.commands

import com.ryderbelserion.api.command.CommandContext
import com.ryderbelserion.api.command.CommandEngine

class AboutCommand : CommandEngine("about", "Shows information about the bot.") {

    override fun perform(context: CommandContext) {
        context.reply("Guten Tag!")
    }
}