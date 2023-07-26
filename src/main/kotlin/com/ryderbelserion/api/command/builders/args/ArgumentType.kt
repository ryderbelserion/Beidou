package com.ryderbelserion.api.command.builders.args

abstract class ArgumentType {

    abstract fun getPossibleValues(): List<String?>?

}