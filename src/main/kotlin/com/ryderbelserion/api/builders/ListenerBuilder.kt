package com.ryderbelserion.api.builders

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ListenerBuilder(private val jda: JDA?) {

    private fun register(listener: ListenerAdapter) {
        this.jda?.addEventListener(listener)
    }

    fun register(vararg listeners: ListenerAdapter) {
        listeners.forEach(::register)
    }
}