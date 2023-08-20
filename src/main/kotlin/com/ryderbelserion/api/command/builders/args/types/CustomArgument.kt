package com.ryderbelserion.api.command.builders.args.types

import com.ryderbelserion.api.command.builders.args.ArgumentType

class CustomArgument(private val possibleValues: List<String>) : ArgumentType() {

    override fun getPossibleValues(): List<String?> {
        return this.possibleValues
    }
}