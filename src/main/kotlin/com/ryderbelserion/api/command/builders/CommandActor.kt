package com.ryderbelserion.api.command.builders

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User

abstract class CommandActor {

    abstract fun reply(ephemeral: Boolean = false, message: String)

    abstract fun reply(embed: MessageEmbed, ephemeral: Boolean = false)

    abstract fun author(): User?

    abstract fun creator(): User?

    abstract fun bot(): SelfUser

    abstract fun guild(): Guild?

    abstract fun jda(): JDA

}