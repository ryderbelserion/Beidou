package com.ryderbelserion.api.options.types

import com.ryderbelserion.api.options.interfaces.IOption
import joptsimple.OptionParser
import joptsimple.OptionSet
import java.util.Optional

class TokenOption(private val parser: OptionParser, override val description: String, override val name: String) : IOption<String> {

    override fun getValue(option: OptionSet): Optional<String> {
        return Optional.ofNullable(option.valueOf(this.name).toString())
    }

    override fun init(): IOption<String> {
        this.parser.accepts(this.name).withRequiredArg().ofType(String::class.java)
            .defaultsTo("token_not_found").describedAs(this.description)

        return this
    }
}