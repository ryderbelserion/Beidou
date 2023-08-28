package com.ryderbelserion.bot.commands.mod

import com.ryderbelserion.api.command.CommandContext
import com.ryderbelserion.api.command.CommandEngine
import net.dv8tion.jda.api.Permission

class MuteCommand : CommandEngine("mute", "Mutes a discord member.", Permission.MODERATE_MEMBERS) {

    override fun perform(context: CommandContext) {
        TODO("Not yet implemented")
    }
}