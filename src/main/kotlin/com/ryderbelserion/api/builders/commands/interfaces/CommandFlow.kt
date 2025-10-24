package com.ryderbelserion.api.builders.commands.interfaces

import com.ryderbelserion.api.builders.commands.CommandEngine
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

interface CommandFlow {

    fun addCommand(engine: CommandEngine)

    fun addCommand(engine: CommandEngine, optionData: OptionData)

    fun addCommand(engine: CommandEngine, optionData: List<OptionData>)

    fun addCommand(engine: CommandEngine, type: OptionType, name: String, description: String)

    fun addGuildCommand(engine: CommandEngine)

    fun addGuildCommand(engine: CommandEngine, optionData: OptionData)

    fun addGuildCommand(engine: CommandEngine, optionData: List<OptionData>)

    fun addGuildCommand(engine: CommandEngine, type: OptionType, name: String, description: String)

    fun addGuildCommands(vararg engine: CommandEngine)

    fun addCommands(vararg engine: CommandEngine)

    fun purgeGuildCommands()

    fun purgeGlobalCommands()

}