package com.ryderbelserion.api.command.builders.args.types

import com.ryderbelserion.api.command.builders.args.ArgumentType

class BooleanArgument : ArgumentType() {

    override fun getPossibleValues(): List<String?> {
        return listOf("true", "false")
    }
}