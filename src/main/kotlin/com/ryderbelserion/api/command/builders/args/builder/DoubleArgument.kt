package com.ryderbelserion.api.command.builders.args.builder

import com.ryderbelserion.api.command.builders.args.ArgumentType
import java.text.DecimalFormat

class DoubleArgument(private val max: Double = 100.0) : ArgumentType() {

    override fun getPossibleValues(): List<String> {
        val numbers: MutableList<String> = ArrayList()
        val decimalFormat = DecimalFormat("0.0")

        var value = 0.1

        while (value <= max) {
            val formattedNumber: String = decimalFormat.format(value)
            numbers.add(formattedNumber)
            value += 0.1
        }

        return numbers
    }
}