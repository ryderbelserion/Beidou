package com.ryderbelserion.api.command.builders.args.builder

import com.ryderbelserion.api.command.builders.args.ArgumentType
import java.text.DecimalFormat

class FloatArgument(private val max: Float = 100f) : ArgumentType() {

    override fun getPossibleValues(): List<String> {
        val numbers: MutableList<String> = ArrayList()
        val decimalFormat = DecimalFormat("0.0")

        var value = 0.1f

        while (value <= max) {
            val formattedNumber: String = decimalFormat.format(value)
            numbers.add(formattedNumber)
            value += 0.1f
        }

        return numbers
    }
}