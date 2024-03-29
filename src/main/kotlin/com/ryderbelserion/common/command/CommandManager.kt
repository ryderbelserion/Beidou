package com.ryderbelserion.common.command

import com.ryderbelserion.common.command.interfaces.CommandFlow
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

class CommandManager : CommandFlow() {

    private lateinit var jda: JDA
    private lateinit var guild: Guild

    /**
     * Sets the jda instance.
     */
    fun setJDA(jda: JDA): CommandManager {
        this.jda = jda

        return this
    }

    /**
     * Sets the guild to add slash commands to.
     */
    fun setGuild(guild: Guild): CommandManager {
        this.guild = guild

        return this
    }

    /**
     * Adds a single global slash command.
     */
    override fun addCommand(engine: CommandEngine) {
        this.jda.upsertCommand(engine.name, engine.desc).queue()
    }

    /**
     * Adds a single slash command to guilds.
     */
    override fun addGuildCommand(engine: CommandEngine) {
        this.guild.upsertCommand(engine.name, engine.desc).queue()
    }

    /**
     * Adds multiple slash commands to guilds.
     */
    fun addGuildCommands(vararg engine: CommandEngine) {
        engine.forEach(::addGuildCommand)
    }

    /**
     * Removes all slash commands from guilds.
     */
    fun clearGuildCommands() {
        this.guild.updateCommands().queue()
    }

    /**
     * Removes all global slash commands.
     */
    fun clearGlobalCommands() {
        this.jda.updateCommands().queue()
    }
}