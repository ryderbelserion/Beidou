package com.ryderbelserion.api.builders.commands.interfaces

import com.ryderbelserion.api.builders.commands.CommandEngine
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

interface CommandFlow {

    fun addCommand(engine: CommandEngine)

    fun addCommand(engine: CommandEngine, optionData: OptionData)

    fun addCommand(engine: CommandEngine, optionDataList: MutableList<OptionData?>)

    fun addCommand(engine: CommandEngine, type: OptionType, name: String, description: String)

    fun addGuildCommand(guild: Guild, engine: CommandEngine)

    fun addGuildCommand(guild: Guild, engine: CommandEngine, optionData: OptionData)

    fun addGuildCommand(guild: Guild, engine: CommandEngine, optionDataList: MutableList<OptionData?>)

    fun addGuildCommand(guild: Guild, engine: CommandEngine, type: OptionType, name: String, description: String)

    fun addGuildCommands(guild: Guild, vararg engines: CommandEngine)

    fun addCommands(vararg engines: CommandEngine)

    fun purgeGuildCommands(guild: Guild)

    fun purgeGlobalCommands()

}