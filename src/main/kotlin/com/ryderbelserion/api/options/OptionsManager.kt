package com.ryderbelserion.api.options

import com.ryderbelserion.api.options.interfaces.IOption
import com.ryderbelserion.api.options.types.TokenOption
import joptsimple.OptionParser
import joptsimple.OptionSet
import java.util.Collections
import java.util.Optional

class OptionsManager {

    private val options = hashMapOf<String, IOption<*>>()

    private val parser: OptionParser = OptionParser()

    private var option: Optional<OptionSet> = Optional.empty()

    fun init(args: Array<String>) {
        listOf(
            TokenOption(this.parser, "Provides a discord bot token to the application.", "token")
        ).forEach { option ->
            this.options[option.name] = option.init()
        }

        this.option = Optional.of(this.parser.parse(*args))
    }

    fun getOptionByName(name: String): Optional<IOption<*>> {
        return Optional.ofNullable(this.options[name])
    }

    fun getOptions(): Map<String, IOption<*>?> {
        return Collections.unmodifiableMap(this.options)
    }

    fun getOptionSet(): Optional<OptionSet> {
        return this.option
    }
}