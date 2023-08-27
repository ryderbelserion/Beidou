package com.ryderbelserion.api.command.builders

abstract class CommandFlow {

    abstract fun addCommand(name: String, description: String)
    abstract fun addGuildCommand(name: String, description: String)

}