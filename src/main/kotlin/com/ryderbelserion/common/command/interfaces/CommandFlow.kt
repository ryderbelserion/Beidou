package com.ryderbelserion.common.command.interfaces

import com.ryderbelserion.common.command.CommandEngine

abstract class CommandFlow {

    abstract fun addCommand(engine: CommandEngine)
    abstract fun addGuildCommand(engine: CommandEngine)

}