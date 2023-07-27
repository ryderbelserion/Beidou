package com.ryderbelserion.api.command

import com.ryderbelserion.api.command.builders.CommandFlow
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

class CommandManager : CommandFlow() {

    private lateinit var jda: JDA
    private lateinit var guild: Guild

    fun setJDA(jda: JDA): CommandManager {
        this.jda = jda

        return this
    }

    fun setGuild(guild: Guild): CommandManager {
        this.guild = guild

        return this
    }

    override fun addCommand(engine: CommandEngine) {
        jda.upsertCommand(engine.name, engine.desc).queue()
    }

    override fun addGuildCommand(engine: CommandEngine) {
        guild.upsertCommand(engine.name, engine.desc).queue()
    }
}