package com.ryderbelserion.api.command.builders.args.types

import com.ryderbelserion.api.command.builders.args.ArgumentType

class IntArgument(cap: Int) : ArgumentType() {

    private var cap: Int

    init {
        if (cap <= 0) {
            this.cap = 100
        } else {
            this.cap = cap
        }
    }

    override fun getPossibleValues(): List<String?> {
        val numbers: MutableList<String> = ArrayList()
        var value = 1

        while (value <= this.cap) {
            numbers.add(value.toString())
            value += 1
        }

        return numbers
    }
}