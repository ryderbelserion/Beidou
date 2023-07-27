package com.ryderbelserion.api.command.builders

import com.ryderbelserion.api.command.CommandEngine

abstract class CommandFlow {

    abstract fun addCommand(engine: CommandEngine)
    abstract fun addGuildCommand(engine: CommandEngine)

}