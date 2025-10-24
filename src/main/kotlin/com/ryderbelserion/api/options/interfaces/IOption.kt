package com.ryderbelserion.api.options.interfaces

import joptsimple.OptionSet
import java.util.Optional

interface IOption<T> {

    val description: String?

    val name: String?

    fun getValue(option: OptionSet): Optional<T>

    fun init(): IOption<T>
}