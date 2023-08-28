package com.ryderbelserion.bot.persist.data

import com.ryderbelserion.api.command.CommandEngine
import com.ryderbelserion.api.plugin.config.FileEngine
import com.ryderbelserion.api.plugin.config.types.ConfigType
import net.dv8tion.jda.api.Permission
import java.io.File

class Guilds(file: File) : FileEngine("guild.json", file.toPath(), ConfigType.JSON) {

    private val commands = hashMapOf<String, Command>()

    fun addCommand(engine: CommandEngine) {
        this.commands.putIfAbsent(engine.name, Command(null))
    }

    data class Command(val permission: Permission?)

}