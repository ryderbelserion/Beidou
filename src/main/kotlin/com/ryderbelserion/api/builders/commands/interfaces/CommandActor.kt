package com.ryderbelserion.api.builders.commands.interfaces

import com.ryderbelserion.api.builders.commands.CommandContext
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionMapping

interface CommandActor {

    fun reply(message: String, ephemeral: Boolean)

    fun reply(message: MessageEmbed, ephemeral: Boolean)

    fun defer(ephemeral: Boolean): CommandContext

    fun getOption(option: String): OptionMapping?

    fun author(): User?

    fun creator(): User?

    fun bot(): SelfUser

    fun guild(): Guild?

    fun jda(): JDA

}