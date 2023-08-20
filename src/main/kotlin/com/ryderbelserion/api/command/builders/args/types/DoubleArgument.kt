package com.ryderbelserion.api.command.builders.args.types

import com.ryderbelserion.api.command.builders.args.ArgumentType
import java.text.DecimalFormat

class DoubleArgument(cap: Double) : ArgumentType() {

    private var cap: Double

    init {
        if (cap <= 0.0) {
            this.cap = 100.0
        } else {
            this.cap = cap
        }
    }

    override fun getPossibleValues(): List<String?> {
        val numbers: MutableList<String> = ArrayList()
        val decimalFormat = DecimalFormat("0.0")
        var value = 0.1

        while (value <= cap) {
            val formattedNumber: String = decimalFormat.format(value)
            numbers.add(formattedNumber)
            value += 0.1
        }

        return numbers
    }
}