package com.ryderbelserion.api.builders.commands

import com.ryderbelserion.api.builders.commands.interfaces.CommandFlow
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class CommandHandler(private val jda: JDA) : CommandFlow {

    private lateinit var guild: Guild

    override fun addCommand(engine: CommandEngine) {
        this.jda.upsertCommand(engine.name, engine.description).queue()
    }

    override fun addCommand(engine: CommandEngine, optionData: OptionData) {
        this.jda.upsertCommand(engine.name, engine.description).addOptions(optionData).queue()
    }

    override fun addCommand(engine: CommandEngine, optionDataList: MutableList<OptionData?>) {
        this.jda.upsertCommand(engine.name, engine.description).addOptions(optionDataList).queue()
    }

    override fun addCommand(engine: CommandEngine, type: OptionType, name: String, description: String) {
        this.jda.upsertCommand(engine.name, engine.description).addOption(type, name, description).queue()
    }

    override fun addGuildCommand(guild: Guild, engine: CommandEngine) {
        guild.upsertCommand(engine.name, engine.description).queue()
    }

    override fun addGuildCommand(guild: Guild, engine: CommandEngine, optionData: OptionData) {
        guild.upsertCommand(engine.name, engine.description).addOptions(optionData).queue()
    }

    override fun addGuildCommand(guild: Guild, engine: CommandEngine, optionDataList: MutableList<OptionData?>) {
        guild.upsertCommand(engine.name, engine.description).addOptions(optionDataList).queue()
    }

    override fun addGuildCommand(
        guild: Guild,
        engine: CommandEngine,
        type: OptionType,
        name: String,
        description: String
    ) {
        guild.upsertCommand(engine.name, engine.description).addOption(type, name, description).queue()
    }

    override fun addGuildCommands(guild: Guild, vararg engines: CommandEngine) {
        for (engine in engines) {
            addGuildCommand(guild, engine)
        }
    }

    override fun addCommands(vararg engines: CommandEngine) {
        for (engine in engines) {
            addCommand(engine)
        }
    }

    override fun purgeGuildCommands(guild: Guild) {
        guild.updateCommands().queue()
    }

    override fun purgeGlobalCommands() {
        this.jda.updateCommands().queue()
    }

    fun guild(guild: Guild): CommandHandler {
        this.guild = guild

        return this
    }
}