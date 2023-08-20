package com.ryderbelserion.api.command.builders.args.types

import com.ryderbelserion.api.command.builders.args.ArgumentType

class LongArgument(cap: Long) : ArgumentType() {

    private var cap: Long

    init {
        if (cap <= 0L) {
            this.cap = 100L
        } else {
            this.cap = cap
        }
    }

    override fun getPossibleValues(): List<String?> {
        val numbers: MutableList<String> = ArrayList()
        var value = 1L

        while (value <= this.cap) {
            numbers.add(value.toString())
            value += 1L
        }

        return numbers
    }
}