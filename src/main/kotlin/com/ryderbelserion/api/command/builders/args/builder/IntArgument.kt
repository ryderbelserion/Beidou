package com.ryderbelserion.api.command.builders.args.builder

import com.ryderbelserion.api.command.builders.args.ArgumentType

class IntArgument(private val max: Int = 100) : ArgumentType() {

    override fun getPossibleValues(): List<String> {
        val numbers: MutableList<String> = ArrayList()

        var value = 1

        while (value <= max) {
            numbers.add(value.toString())
            value += 1
        }

        return numbers
    }
}