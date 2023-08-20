package com.ryderbelserion.api.command.builders.args.types

import com.ryderbelserion.api.command.builders.args.ArgumentType
import java.text.DecimalFormat

class FloatArgument(cap: Float) : ArgumentType() {

    private val cap: Float

    init {
        if (cap <= 0f) {
            this.cap = 100f
        } else {
            this.cap = cap
        }
    }

    override fun getPossibleValues(): List<String?>? {
        val numbers: MutableList<String> = ArrayList()
        val decimalFormat = DecimalFormat("0.0f")
        var value = 0.1f

        while (value <= cap) {
            val formattedValue: String = decimalFormat.format(value.toDouble())
            numbers.add(formattedValue)
            value += 0.1f
        }

        return numbers
    }
}